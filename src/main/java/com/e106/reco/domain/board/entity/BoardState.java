package com.e106.reco.domain.board.entity;

import com.e106.reco.global.error.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

import static com.e106.reco.global.error.errorcode.BoardErrorCode.BOARD;

@Getter
@RequiredArgsConstructor
public enum BoardState {
    PUBLIC("공개"),
    PRIVATE("비공개"),
    INACTIVE("비활성");
    private final String name;

    @JsonCreator
    public static BoardState of(String inputValue) {
        return Stream.of(BoardState.values())
                .filter(BoardState -> BoardState.name.equals(inputValue))
                .findFirst()
                .orElseThrow(() -> new BusinessException(BOARD));
    }
}