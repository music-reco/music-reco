package com.e106.reco.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {
    CHAT_GRANT_FAIL(HttpStatus.BAD_REQUEST, "채팅을 요청한 유저와 현재 유저는 다른 유저입니다"),
    CHAT_NOT_ALLOW_GROUP_CHAT(HttpStatus.BAD_REQUEST, "그룹 채팅이 허용되지 않습니다"),
    SINGLE_CHAT_ONLY_ONE_RECEIVER(HttpStatus.BAD_REQUEST, "개인 채팅은 한 명의 받는 사람이 있는 채팅입니다"),
    ARTIST_NOT_IN_CHAT(HttpStatus.BAD_REQUEST,"채팅방에 있지 않은 사용자 입니다."),
    ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 채팅방입니다"),

    POSITION(HttpStatus.BAD_REQUEST, "포지션이 입력되지 않았거나 데이터베이스 내 값이 아닙니다"),
    REGION(HttpStatus.BAD_REQUEST, "지역이 입력되지 않았거나 데이터베이스 내 값이 아닙니다"),
    GENRE(HttpStatus.BAD_REQUEST, "장르가 입력되지 않았거나 데이터베이스 내 값이 아닙니다");

    private final HttpStatus httpStatus;
    private final String message;
}