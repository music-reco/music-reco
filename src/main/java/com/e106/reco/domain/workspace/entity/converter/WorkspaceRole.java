package com.e106.reco.domain.workspace.entity.converter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkspaceRole {
    VIEWER("읽기 허용"),
    MASTER("전체 허용");
    private final String name;
}
