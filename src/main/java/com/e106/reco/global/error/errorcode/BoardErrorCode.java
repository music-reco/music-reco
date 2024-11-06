package com.e106.reco.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    BOARD_SOURCE_ONLY_15(HttpStatus.BAD_REQUEST, "사진은 15개까지만 입력이 가능합니다"),
    BOARD_GRANT_FAIL(HttpStatus.BAD_REQUEST, "작성권한이 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}