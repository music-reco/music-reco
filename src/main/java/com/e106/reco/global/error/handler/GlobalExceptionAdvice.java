package com.e106.reco.global.error.handler;

import com.e106.reco.global.error.errorcode.ErrorCode;
import com.e106.reco.global.error.exception.BusinessException;
import com.e106.reco.global.error.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.e106.reco.global.error.errorcode.CommonErrorCode.INVALID_PARAMETER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(errorCode, e.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse(INVALID_PARAMETER, "입력값이 잘못 되었습니다.");
        errorResponse.addFieldErrors(e.getBindingResult());
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }
}
