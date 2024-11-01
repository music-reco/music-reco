package com.e106.reco.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CrewErrorCode implements ErrorCode {
    GRANT_CHAT_ONLY_ONE(HttpStatus.BAD_REQUEST, "채팅 권한은 마스터와 한명만 줄 수 있습니다"),


    CREW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 크루입니다"),

    USER_ALREADY_JOIN(HttpStatus.BAD_REQUEST, "이미 신청 했거나 회원입니다"),
    USER_NOT_WAIT(HttpStatus.BAD_REQUEST, "가입을 기다리는 회원이 아닙니다"),
    CREW_ALREADY_FULL(HttpStatus.BAD_REQUEST, "크루의 정원이 다 찼습니다"),

    CREW_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "가입하지 않은 유저입니다"),

    USER_IS_MASTER(HttpStatus.NOT_FOUND, "유저는 크루의 장입니다"),
    USER_NOT_MASTER(HttpStatus.NOT_FOUND, "유저는 크루의 장이 아닙니다"),
    USER_EXIST(HttpStatus.BAD_REQUEST,"이미 가입한 크루입니다");

    private final HttpStatus httpStatus;
    private final String message;
}