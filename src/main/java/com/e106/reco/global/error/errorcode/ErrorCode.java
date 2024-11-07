package com.e106.reco.global.error.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();

    HttpStatus getHttpStatus();
    String getMessage();
}
