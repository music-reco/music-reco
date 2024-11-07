package com.e106.reco.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    BOARD(HttpStatus.BAD_REQUEST, "게시글의 상태가 올바르게 입력되지 않았습니다"),
    BOARD_NOT_FOUND(HttpStatus.BAD_REQUEST, "게시글이 존재하지 않습니다"),
    BOARD_SOURCE_ONLY_15(HttpStatus.BAD_REQUEST, "사진은 15개까지만 입력이 가능합니다"),
    BOARD_GRANT_FAIL(HttpStatus.BAD_REQUEST, "작성권한이 없습니다"),

    COMMENT_DEEP_ONLY_ONE(HttpStatus.BAD_REQUEST, "대댓글은 한 번만 달 수 있습니다"),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "댓글을 찾을 수 없습니다");
    private final HttpStatus httpStatus;
    private final String message;
}