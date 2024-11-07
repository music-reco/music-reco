package com.e106.reco.global.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode {
    FILE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "파일 업로드에 실패하였습니다"),
    FILE_EXE_ERROR(HttpStatus.BAD_REQUEST, "파일 확장자가 문제가 있습니다"),
    FILE_NAME_ERROR(HttpStatus.BAD_REQUEST, "파일 이름에 문제가 있습니다");

    private final HttpStatus httpStatus;
    private final String message;
}