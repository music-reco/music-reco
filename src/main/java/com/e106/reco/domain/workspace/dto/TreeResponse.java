package com.e106.reco.domain.workspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TreeResponse {
    private String originTitle;
    private String originSinger;
    private List<TreeInfoResponse> treeInfoResponseList;
}
