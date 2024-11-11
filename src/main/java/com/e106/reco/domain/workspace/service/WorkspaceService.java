package com.e106.reco.domain.workspace.service;

import com.e106.reco.domain.workspace.dto.SoundResponse;
import com.e106.reco.domain.workspace.dto.TreeResponse;
import com.e106.reco.domain.workspace.dto.WorkspaceDetailResponse;
import com.e106.reco.domain.workspace.dto.WorkspaceRequest;
import com.e106.reco.domain.workspace.dto.WorkspaceResponse;
import com.e106.reco.domain.workspace.dto.divide.AudioDivideResponse;
import com.e106.reco.domain.workspace.dto.midify.ModifyPoint;
import com.e106.reco.domain.workspace.dto.midify.ModifyState;
import com.e106.reco.domain.workspace.entity.FamilyTree;
import com.e106.reco.domain.workspace.entity.Sound;
import com.e106.reco.domain.workspace.entity.Workspace;
import com.e106.reco.domain.workspace.entity.converter.SoundType;
import com.e106.reco.domain.workspace.entity.converter.WorkspaceRole;
import com.e106.reco.domain.workspace.entity.converter.WorkspaceState;
import com.e106.reco.domain.workspace.repository.FamilyTreeRepository;
import com.e106.reco.domain.workspace.repository.SoundRepository;
import com.e106.reco.domain.workspace.repository.WorkspaceRepository;
import com.e106.reco.global.common.CommonResponse;
import com.e106.reco.global.error.errorcode.WorkspaceErrorCode;
import com.e106.reco.global.error.exception.BusinessException;
import com.e106.reco.global.s3.S3FileService;
import com.e106.reco.global.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final SoundRepository soundRepository;
    private final FamilyTreeRepository familyTreeRepository;
    private final S3FileService s3FileService;
    private final DivideService divideService;

    public Long create(WorkspaceRequest workspaceRequest) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace workspace = Workspace.of(workspaceRequest, artistSeq);

        return workspaceRepository.save(workspace).getSeq();
    }

    @Async
    public CompletableFuture<List<AudioDivideResponse>> divide(WorkspaceRequest workspaceRequest,
                                                               MultipartFile file, List<String> stemList, String splitter) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();
        Workspace workspace = Workspace.of(workspaceRequest, artistSeq);

        String contentType = file.getContentType();
        log.info("Original ContentType: {}", contentType);

        return processAudioFile(file, contentType, stemList, splitter, workspace);
    }

    private CompletableFuture<List<AudioDivideResponse>> processAudioFile(
            MultipartFile file, String contentType, List<String> stemList, String splitter, Workspace workspace) {
        File tempFile = null;
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".tmp";

            tempFile = File.createTempFile("audio_", extension);
            file.transferTo(tempFile);
            workspaceRepository.save(workspace);
            FileInfo fileInfo = new FileInfo(tempFile, contentType);

            List<CompletableFuture<AudioDivideResponse>> futures = stemList.stream()
                    .map(stem -> divideService.divideAudioFile(fileInfo.file, fileInfo.contentType, stem, splitter)
                            .thenApply(response -> {
                                saveSound(response, workspace, stem);
                                return response;
                            })
                            .exceptionally(ex -> {
                                log.error("Error processing stem: {}", stem, ex);
                                return null;
                            }))
                    .toList();

            File finalTempFile = tempFile;
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList()))
                    .whenComplete((result, throwable) -> {
                        if (finalTempFile.exists()) {
                            finalTempFile.delete();
                        }
                    });
        } catch (Exception e) {
            log.error("Error in divide process", e);
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
            throw new RuntimeException("Failed to process audio file", e);
        }
    }
    @Transactional(readOnly = true)
    public List<WorkspaceResponse> getWorkspaceList(Long artistSeq, Pageable pageable) {

        if(!Objects.equals(artistSeq, AuthUtil.getCustomUserDetails().getSeq()))
            throw new BusinessException(WorkspaceErrorCode.ROLE_NOT_MATCHED);

        return workspaceRepository.findByArtistSeqAndStateNot(artistSeq, WorkspaceState.INACTIVE, pageable)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public WorkspaceDetailResponse getWorkspaceDetail(Long workspaceSeq) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace workspace = getWorkspace(workspaceSeq, artistSeq);

        WorkspaceRole role = getRole(workspace);

        if (role == WorkspaceRole.VIEWER && workspace.getState() != WorkspaceState.PUBLIC)
            throw new BusinessException(WorkspaceErrorCode.NOT_PUBLIC_WORKSPACE);

        List<Sound> sounds = getSounds(workspaceSeq);

        return toDetailResponse(workspace, sounds, role);

    }

    public Long fork(Long workspaceSeq) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace originWorkspace = getWorkspace(workspaceSeq, artistSeq);

        Workspace newWorkspace = Workspace.fork(originWorkspace, artistSeq);

        Long newSeq = workspaceRepository.saveAndFlush(newWorkspace).getSeq();

        getSounds(workspaceSeq).forEach(originSound -> {
            Sound sound = Sound.fork(originSound, newSeq);
            soundRepository.save(sound);
        });
        FamilyTree familyTree = FamilyTree.builder()
                .pk(new FamilyTree.PK(workspaceSeq, newSeq))
                .parentWorkspace(originWorkspace)
                .childWorkspace(newWorkspace)
                .build();
        familyTreeRepository.save(familyTree);

        return newSeq;
    }

    public CommonResponse sessionImport(Long workspaceSeq, Long soundSeq) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace workspace = getWorkspace(workspaceSeq, artistSeq);

        Sound sound = soundRepository.findById(soundSeq)
                .orElseThrow(() -> new BusinessException(WorkspaceErrorCode.SOUND_NOT_FOUND));

        Sound newSound = Sound.fork(sound, workspace.getSeq());

        soundRepository.save(newSound);

        return new CommonResponse("ok");
    }
    public CommonResponse modifyPoint(Long workspaceSeq,
                                      List<ModifyPoint> modifyPoints) {
        Long userSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace workspace = getWorkspace(workspaceSeq, userSeq);

        Map<Long, Sound> sounds = getSounds(workspaceSeq).stream()
                .collect(Collectors.toMap(Sound::getSeq, Function.identity()));

        modifyPoints.forEach(modifyPoint -> {
            Sound sound = sounds.get(modifyPoint.getSoundSeq());
            sound.modifyPoint(modifyPoint, workspace);
        });

        return new CommonResponse("ok");
    }


    public CommonResponse modifyState(Long workspaceSeq, ModifyState modifyState) {

        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace workspace = getWorkspace(workspaceSeq, artistSeq);

        workspace.modifyState(modifyState.getState());

        return new CommonResponse("ok");
    }

    public List<TreeResponse> getTree(Long workspaceSeq) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace workspace = getWorkspace(workspaceSeq, artistSeq);

        return familyTreeRepository.findAllByPk_ChildWorkspaceSeqOrderByCreatedAt(workspace.getSeq())
                .stream()
                .map(this::toTreeResponse)
                .toList();
    }
    public CommonResponse modifyThumbnail(Long workspaceSeq, MultipartFile file) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace workspace = getWorkspace(workspaceSeq, artistSeq);

        workspace.modifyThumbnail(s3FileService.uploadFile(file));

        return new CommonResponse("ok");
    }

    private WorkspaceRole getRole(Workspace workspace) {
        if (Objects.equals(workspace.getArtist().getSeq(),
                AuthUtil.getCustomUserDetails().getSeq())) return WorkspaceRole.MASTER;

        return WorkspaceRole.VIEWER;
    }
    public CommonResponse sessionCreate(Long workspaceSeq, MultipartFile session) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace workspace = getWorkspace(workspaceSeq, artistSeq);

        String fileName = s3FileService.getFile(s3FileService.uploadFile(session));

        Sound sound = Sound.of(fileName, workspace.getSeq());

        soundRepository.save(sound);

        return new CommonResponse("ok");
    }

    public CommonResponse sessionDelete(Long workspaceSeq, Long sessionSeq) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace workspace = getWorkspace(workspaceSeq, artistSeq);

        Sound sound = soundRepository.findById(sessionSeq)
                .orElseThrow(() -> new BusinessException(WorkspaceErrorCode.SOUND_NOT_FOUND));

        soundRepository.delete(sound);

        return new CommonResponse("ok");
    }
    private List<Sound> getSounds(Long workspaceSeq) {
        return soundRepository.findAllByWorkspace_Seq(workspaceSeq);
    }

    private Workspace getWorkspace(Long workspaceSeq, Long artistSeq) {
        Workspace workspace = workspaceRepository.findById(workspaceSeq)
                .orElseThrow(() -> new BusinessException(WorkspaceErrorCode.NOT_PUBLIC_WORKSPACE));

        if(!Objects.equals(workspace.getArtist().getSeq(), artistSeq))
            throw new BusinessException(WorkspaceErrorCode.ROLE_NOT_MATCHED);

        return workspace;
    }

    private WorkspaceDetailResponse toDetailResponse(Workspace workspace, List<Sound> sounds, WorkspaceRole role) {
        return WorkspaceDetailResponse.builder()
                .workspaceSeq(workspace.getSeq())
                .name(workspace.getName())
                .originSinger(workspace.getOriginSinger())
                .originTitle(workspace.getOriginTitle())
                .state(workspace.getState())
                .sounds(sounds.stream().map(this::toSoundResponse).toList())
                .role(role)
                .thumbnail(s3FileService.getFile(workspace.getThumbnail()))
                .createdAt(workspace.getCreatedAt())
                .updatedAt(workspace.getUpdatedAt())
                .build();
    }

    private WorkspaceResponse toResponse(Workspace workspace) {
        return WorkspaceResponse.builder()
                .workspaceSeq(workspace.getSeq())
                .name(workspace.getName())
                .state(workspace.getState())
                .thumbnail(s3FileService.getFile(workspace.getThumbnail()))
                .originTitle(workspace.getOriginTitle())
                .originSinger(workspace.getOriginSinger())
                .build();
    }

    private SoundResponse toSoundResponse(Sound sound) {
        return SoundResponse.builder()
                .soundSeq(sound.getSeq())
                .startPoint(sound.getStartPoint())
                .endPoint(sound.getEndPoint())
                .url(sound.getUrl())
                .type(sound.getType())
                .build();
    }

    private TreeResponse toTreeResponse(FamilyTree familyTree) {
        return TreeResponse.builder()
                .workspaceSeq(familyTree.getPk().getParentWorkspaceSeq())
                .artistName(familyTree.getParentWorkspace().getArtist().getNickname())
                .workspaceName(familyTree.getParentWorkspace().getName())
                .build();
    }

    private void saveSound(AudioDivideResponse response, Workspace workspace, String stemType) {
        // stemTrack 저장
        if (response.getStemTrackUrl() != null) {
            Sound stemTrack = Sound.builder()
                    .url(response.getStemTrackUrl())
                    .type(SoundType.fromString(stemType))  // enum으로 변환
                    .workspace(workspace)
                    .startPoint(0d)
                    .endPoint(response.getDuration())
                    .build();
            soundRepository.save(stemTrack);
        }
    }

    private static class FileInfo {
        final File file;
        final String contentType;

        FileInfo(File file, String contentType) {
            this.file = file;
            this.contentType = contentType;
        }
    }
}
