package com.e106.reco.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WorkspaceErrorCode implements ErrorCode {
    WORKSPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "워크 스페이스를 찾지 못했습니다."),
    NOT_PUBLIC_WORKSPACE(HttpStatus.BAD_REQUEST, "공유된 작업물이 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
