package com.example.authenservice.exception;

import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.exception.ErrorCode;
import com.example.commericalcommon.utils.MessageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationException {
    MessageUtil messageUtil;

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<BaseResponse<Object>> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(setBaseResponse(errorCode.getCode(), errorCode.getMessage()));
    }

    private BaseResponse<Object> setBaseResponse(String errorCode, String message) {
        return BaseResponse.builder()
                .resultDesc((messageUtil.getMessage(message)))
                .resultCode(errorCode)
                .build();
    }
}
