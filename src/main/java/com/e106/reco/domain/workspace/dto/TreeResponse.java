package com.e106.reco.domain.workspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TreeResponse {
    private Long workspaceSeq;
    private String workspaceName;
    private String artistName;
}
