package com.e106.reco.domain.workspace.entity.converter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkspaceState {
    PUBLIC("공개"),
    PRIVATE("비공개"),
    INACTIVE("비활성");
    private final String state;
}
