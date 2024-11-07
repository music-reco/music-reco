package com.e106.reco.domain.board.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentState {
    ACTIVE("활성"),
    INACTIVE("비활성");
    private final String name;
}