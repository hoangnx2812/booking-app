package com.example.commericalcommon.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 624801615985179478L;

    @Builder.Default
    String resultCode = "00";

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
