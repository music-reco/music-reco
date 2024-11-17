package com.e106.reco.domain.workspace.service;

import com.e106.reco.domain.workspace.dto.SoundResponse;
import com.e106.reco.domain.workspace.dto.TreeInfoResponse;
import com.e106.reco.domain.workspace.dto.TreeResponse;
import com.e106.reco.domain.workspace.dto.WorkspaceDetailResponse;
import com.e106.reco.domain.workspace.dto.WorkspaceDto;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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

    @Value("${spring.servlet.multipart.location}")
    private String tempPath;

    public Long create(WorkspaceRequest workspaceRequest) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace workspace = Workspace.of(workspaceRequest, artistSeq);

        return workspaceRepository.save(workspace).getSeq();
    }

    @Async(value = "asyncExecutor2")
    public CompletableFuture<List<AudioDivideResponse>> divide(WorkspaceRequest workspaceRequest,
                                                               MultipartFile file, List<String> stemList, String splitter) {
        log.info("divide start...");
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();
        Workspace w = Workspace.of(workspaceRequest, artistSeq);
        Workspace workspace = workspaceRepository.saveAndFlush(w);
        log.info("w워크스페이스 생성 했다? : {}", w.getSeq());
        log.info("workspace워크스페이스 생성 했다? : {}", workspace.getSeq());
//        log.info();
        String contentType = file.getContentType();
        log.info("Original ContentType: {}", contentType);

        return processAudioFile(file, contentType, stemList, splitter, workspace);
    }

    private CompletableFuture<List<AudioDivideResponse>> processAudioFile(
            MultipartFile file, String contentType, List<String> stemList, String splitter, Workspace workspace) {
        File tempDir = new File(tempPath);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        File tempFile = null;
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".tmp";

            tempFile = File.createTempFile("audio_", extension, tempDir);

            try (InputStream in = file.getInputStream();
                 FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }

            log.info("Temporary file created at: {}", tempFile.getAbsolutePath());
            log.info("workspaceSeq : {}", workspace.getSeq());
            final File audioFile = tempFile;

            List<CompletableFuture<AudioDivideResponse>> futures = stemList.stream()
                    .map(stem -> divideService.divideAudioFile(audioFile, contentType, stem, splitter)
                            .thenApply(response -> {
                                log.info("음악 저장");
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
    public WorkspaceResponse getWorkspaceList(Long artistSeq, Pageable pageable) {

        Page<Workspace> byArtistSeqAndStateNot = workspaceRepository.findByArtistSeqAndStateNot(artistSeq, WorkspaceState.INACTIVE, pageable);
        return toResponse(byArtistSeqAndStateNot.stream()
                .map(this::toDto)
                .toList(), byArtistSeqAndStateNot.getTotalPages());
    }

    @Transactional(readOnly = true)
    public WorkspaceDetailResponse getWorkspaceDetail(Long workspaceSeq) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();
        log.info("artistSeq: {}", artistSeq);
        log.info("workspaceSeq : {}", workspaceSeq);
        Workspace workspace = getWorkspace(workspaceSeq, artistSeq);
        WorkspaceRole role = getRole(workspace);

        log.info("role : {}", role.name());
        log.info("workspaceState : {}", workspace.getState());

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

        familyTreeRepository.findAllByPk_ChildWorkspaceSeqOrderByCreatedAt(workspaceSeq)
                .forEach(familyTree -> {
                    FamilyTree newFamilyTree = FamilyTree.builder()
                            .pk(new FamilyTree.PK(familyTree.getPk().getParentWorkspaceSeq(), newSeq))
                            .parentWorkspace(originWorkspace)
                            .childWorkspace(newWorkspace)
                            .build();
                    familyTreeRepository.save(newFamilyTree);
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

    public TreeResponse getTree(Long workspaceSeq) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace workspace = getWorkspace(workspaceSeq, artistSeq);

        return new TreeResponse(
                workspace.getOriginTitle(),
                workspace.getOriginSinger(),
                familyTreeRepository.findAllByPk_ChildWorkspaceSeqOrderByCreatedAt(workspace.getSeq())
                .stream()
                .map(this::toTreeResponse)
                .toList());
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
        return workspaceRepository.findByWorkspaceSeq(workspaceSeq)
                .orElseThrow(() -> new BusinessException(WorkspaceErrorCode.WORKSPACE_NOT_FOUND));
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

    private WorkspaceDto toDto(Workspace workspace) {
        return WorkspaceDto.builder()
                        .workspaceSeq(workspace.getSeq())
                        .name(workspace.getName())
                        .state(workspace.getState())
                        .thumbnail(s3FileService.getFile(workspace.getThumbnail()))
                        .originTitle(workspace.getOriginTitle())
                        .originSinger(workspace.getOriginSinger())
                        .build();
    }
    private WorkspaceResponse toResponse(List<WorkspaceDto> workspaces, Integer totalPages) {
        return WorkspaceResponse.builder()
                .workspaceDto(workspaces)
                .totalPage(totalPages)
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

    private TreeInfoResponse toTreeResponse(FamilyTree familyTree) {
        return TreeInfoResponse.builder()
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

    // 주기적으로 임시 파일 정리
    @Scheduled(fixedRate = 3600000) // 1시간마다
    public void cleanupTempFiles() {
        File tempDir = new File(tempPath);
        if (tempDir.exists() && tempDir.isDirectory()) {
            File[] files = tempDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (System.currentTimeMillis() - file.lastModified() > 3600000) { // 1시간 이상 된 파일
                        if (!file.delete()) {
                            log.warn("Failed to delete old temporary file: {}", file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
}
