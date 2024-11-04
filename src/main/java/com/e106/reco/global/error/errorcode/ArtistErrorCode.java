package com.e106.reco.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ArtistErrorCode implements ErrorCode {
    GENDER(HttpStatus.BAD_REQUEST, "성별값이 입력되지 않았거나 여성/남성/기타가 아닙니다"),
    POSITION(HttpStatus.BAD_REQUEST, "포지션이 입력되지 않았거나 데이터베이스 내 값이 아닙니다"),
    REGION(HttpStatus.BAD_REQUEST, "지역이 입력되지 않았거나 데이터베이스 내 값이 아닙니다"),
    GENRE(HttpStatus.BAD_REQUEST, "장르가 입력되지 않았거나 데이터베이스 내 값이 아닙니다");

    private final HttpStatus httpStatus;
    private final String message;
}