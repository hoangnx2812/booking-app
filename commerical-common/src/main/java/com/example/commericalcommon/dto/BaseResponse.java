package com.example.commericalcommon.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

import static com.example.commericalcommon.utils.Constant.SUCCESS_CODE;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BaseResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 624801615985179478L;

    @Builder.Default
    String resultCode = SUCCESS_CODE;

    String resultDesc;

    String requestId;

    String requestTime;

    String platform;

    T data;

    String exception;

    String refId;

    String flowId;

    long cost = 0L;
}
