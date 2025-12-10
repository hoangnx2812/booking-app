package com.example.commericalcommon.dto;


import jakarta.validation.Valid;
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
public class BaseRequest<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 3944318010482161888L;

    String requestId;
    String sessionId; // server esmac tự sinh và trả về cho Application Server(AS)
    String clientRequestId; // AS gửi lên, trùng với thiết bị đầu cuối
    String clientSessionId; // AS gửi lên, trùng với thiết bị đầu cuối

    String clientTime; // YYYYMMDDHHMISS.SSS -- AS gửi lên
    String zonedClientTime; // Số milisecond
    String callerUserName; // user AS dùng để đăng nhập n
    String callerPassword;

    Integer userId;
    String userInfoName;
    String userInfoId;
    String userInfoNo;

    String channelCode;

    String deviceId;
    String deviceName;
    String appVersion;

    Integer authorizedMode;
    Integer checkerMode;

    String signature;
    String ip;

    String longitude;// vi tri hien tai
    String latitude;// vi tri hien tai
    String velocity; // vận tốc
    String collectLocationTime; // thời gian thu thập location

    // Add for Firebase
    String registrationId;

    @Valid
    T data;



}
