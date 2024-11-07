package com.e106.reco.domain.workspace.dto;

import com.e106.reco.domain.workspace.entity.converter.WorkspaceRole;
import com.e106.reco.domain.workspace.entity.converter.WorkspaceState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceDetailResponse {

    private Long workspaceSeq;
    private String name;
    private String thumbnail;
    private String originTitle;
    private String originSinger;
    private WorkspaceState state;
    private List<SoundResponse> sounds;
    private WorkspaceRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
