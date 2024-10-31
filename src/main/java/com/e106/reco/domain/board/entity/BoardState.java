package com.e106.reco.domain.board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum BoardState {
    PRIVATE, PUBLIC;

    private int info;
}
