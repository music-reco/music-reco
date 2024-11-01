package com.e106.reco.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CrewErrorCode implements ErrorCode {
    CREW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 크루입니다"),

    USER_ALREADY_JOIN(HttpStatus.BAD_REQUEST, "이미 신청 했거나 회원입니다"),
    USER_NOT_WAIT(HttpStatus.BAD_REQUEST, "가입을 기다리는 회원이 아닙니다"),
    CREW_ALREADY_FULL(HttpStatus.BAD_REQUEST, "크루의 정원이 다 찼습니다"),

    GENDER(HttpStatus.BAD_REQUEST, "성별값이 입력되지 않았거나 여성/남성/기타가 아닙니다"),
    POSITION(HttpStatus.BAD_REQUEST, "포지션이 입력되지 않았거나 데이터베이스 내 값이 아닙니다"),
    REGION(HttpStatus.BAD_REQUEST, "지역이 입력되지 않았거나 데이터베이스 내 값이 아닙니다"),
    GENRE(HttpStatus.BAD_REQUEST, "장르가 입력되지 않았거나 데이터베이스 내 값이 아닙니다"),
    USER_EXIST(HttpStatus.BAD_REQUEST,"이미 회원가입한 이메일 입니다");

    private final HttpStatus httpStatus;
    private final String message;
}