package com.e106.reco.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WorkspaceErrorCode implements ErrorCode {
    ROLE_NOT_MATCHED(HttpStatus.BAD_REQUEST, "본인의 워크스페이스가 아닙니다."),
    WORKSPACE_NOT_FOUND(HttpStatus.BAD_REQUEST, "워크 스페이스를 찾지 못했습니다."),
    NOT_PUBLIC_WORKSPACE(HttpStatus.BAD_REQUEST, "공유된 작업물이 아닙니다."),
    SOUND_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 세션을 찾지 못했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
