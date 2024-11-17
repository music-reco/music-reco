package com.e106.reco.domain.workspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreeInfoResponse {
    private Long workspaceSeq;
    private String workspaceName;
    private String artistName;
}
