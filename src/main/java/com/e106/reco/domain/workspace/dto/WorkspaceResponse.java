package com.e106.reco.domain.workspace.dto;

import com.e106.reco.domain.workspace.entity.converter.WorkspaceState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceResponse {
    private int totalPage;
    private List<WorkspaceDto> workspaceDto;
}
