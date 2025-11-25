package com.example.commericalcommon.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.util.StringUtils;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GlobalException extends RuntimeException {
    ErrorCode errorCode;
    String customMessage;

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GlobalException(ErrorCode errorCode, String customMessage) {
        super(StringUtils.hasText(customMessage) ? customMessage : errorCode.getMessage());
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

}
