package com.e106.reco.domain.workspace.service;

import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.board.repository.ArtistRepository;
import com.e106.reco.domain.workspace.dto.SoundResponse;
import com.e106.reco.domain.workspace.dto.WorkspaceDetailResponse;
import com.e106.reco.domain.workspace.dto.WorkspaceRequest;
import com.e106.reco.domain.workspace.dto.WorkspaceResponse;
import com.e106.reco.domain.workspace.dto.midify.ModifyPoint;
import com.e106.reco.domain.workspace.dto.midify.ModifyState;
import com.e106.reco.domain.workspace.entity.Sound;
import com.e106.reco.domain.workspace.entity.Workspace;
import com.e106.reco.domain.workspace.entity.converter.WorkspaceRole;
import com.e106.reco.domain.workspace.entity.converter.WorkspaceState;
import com.e106.reco.domain.workspace.repository.SoundRepository;
import com.e106.reco.domain.workspace.repository.WorkspaceRepository;
import com.e106.reco.global.common.CommonResponse;
import com.e106.reco.global.error.errorcode.ArtistErrorCode;
import com.e106.reco.global.error.errorcode.WorkspaceErrorCode;
import com.e106.reco.global.error.exception.BusinessException;
import com.e106.reco.global.s3.S3FileService;
import com.e106.reco.global.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceService {

    private final ArtistRepository artistRepository;
    private final WorkspaceRepository workspaceRepository;
    private final SoundRepository soundRepository;
    private final S3FileService s3FileService;


    public Long create(Long artistSeq, WorkspaceRequest workspaceRequest) {
        Artist artist = artistRepository.findById(artistSeq)
                .orElseThrow(() -> new BusinessException(ArtistErrorCode.ARTIST_NOT_FOUND));

        Workspace workspace = Workspace.builder()
                .name(workspaceRequest.getName())
                .artist(artist)
                .originSinger(workspaceRequest.getOriginSinger())
                .originTitle(workspaceRequest.getOriginTitle())
                .state(WorkspaceState.PRIVATE)
                .build();

        return workspaceRepository.save(workspace).getSeq();
    }

//    public Long divide(Long artistSeq, WorkspaceRequest workspaceRequest, MultipartFile sound) {
//
//        // 나누는 api 아마 비동기 메서드 따로 뺴야할듯.
//
//    }

    @Transactional(readOnly = true)
    public List<WorkspaceResponse> getWorkspaceList(Long artistSeq, Pageable pageable) {

        // 유저 크루 검증 코드 필요함

        return workspaceRepository.findByArtistSeqAndStateNot(artistSeq, WorkspaceState.INACTIVE, pageable)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public WorkspaceDetailResponse getWorkspaceDetail(Long workspaceSeq) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace workspace = getWorkspace(workspaceSeq);

        WorkspaceRole role = getRole(workspace);

        if (role == WorkspaceRole.VIEWER && workspace.getState() != WorkspaceState.PUBLIC)
            throw new BusinessException(WorkspaceErrorCode.NOT_PUBLIC_WORKSPACE);

        List<Sound> sounds = getSounds(artistSeq);

        return toDetailResponse(workspace, sounds, role);

    }

    public Long fork(Long workspaceSeq) {
        Long artistSeq = AuthUtil.getCustomUserDetails().getSeq();

        Workspace originWorkspace = getWorkspace(workspaceSeq);

        Workspace newWorkspace = Workspace.fork(originWorkspace, artistSeq);

        Long newSeq = workspaceRepository.saveAndFlush(newWorkspace).getSeq();

        getSounds(workspaceSeq).forEach(originSound -> {
            Sound sound = Sound.fork(originSound, newSeq);
            soundRepository.save(sound);
        });

        return newSeq;
    }

//    public CommonResponse modifyThumbnail(Long workspaceSeq, MultipartFile file) {
//        Workspace workspace = getWorkspace(workspaceSeq);
//
//        workspace
//
//        s3FileService.
//    }

    public CommonResponse modifyPoint(Long workspaceSeq, List<ModifyPoint> modifyPoints) {
        Workspace workspace = getWorkspace(workspaceSeq);

        Map<Long, Sound> sounds = getSounds(workspaceSeq).stream()
                .collect(Collectors.toMap(Sound::getSeq, Function.identity()));

//        modifyPoints.forEach(modifyPoint -> {
//            Sound sound = sounds.getOrDefault(modifyPoint.getSoundSeq(),
//                    Sound.builder()
//                                    .startPoint(p)
//                            build());
//            sound.modifyPoint(modifyPoint);
//        });
        return new CommonResponse("ok");
    }


    public CommonResponse modifyState(Long workspaceSeq, ModifyState modifyState) {
        Workspace workspace = getWorkspace(workspaceSeq);

        workspace.modifyState(modifyState.getState());

        return new CommonResponse("ok");
    }


    private WorkspaceRole getRole(Workspace workspace) {
        if (Objects.equals(workspace.getArtist().getSeq(),
                AuthUtil.getCustomUserDetails().getSeq())) return WorkspaceRole.MASTER;

        // user crew 가져와서 검증하기
//      // List<Long> crewList
        // return true;

        return WorkspaceRole.VIEWER;
    }

    private List<Sound> getSounds(Long artistSeq) {
        return soundRepository.findAllByWorkspace_Seq(artistSeq);
    }

    private Workspace getWorkspace(Long workspaceSeq) {
        return workspaceRepository.findById(workspaceSeq)
                .orElseThrow(() -> new BusinessException(WorkspaceErrorCode.NOT_PUBLIC_WORKSPACE));
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
                .createdAt(workspace.getCreatedAt())
                .updatedAt(workspace.getUpdatedAt())
                .build();
    }

    private WorkspaceResponse toResponse(Workspace workspace) {
        return WorkspaceResponse.builder()
                .workspaceSeq(workspace.getSeq())
                .name(workspace.getName())
                .state(workspace.getState())
                .build();
    }

    private SoundResponse toSoundResponse(Sound sound) {
        return SoundResponse.builder()
                .soundSeq(sound.getSeq())
                .startPoint(sound.getStartPoint())
                .endPoint(sound.getEndPoint())
                .type(sound.getType())
                .build();
    }

}
