package com.example.authenservice.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoResponse {
    Long id;
    String userNo;
    String fullName;
    String email;
    String phoneNumber;
    Integer areaId;
    String addressDetail;
    String avatar;
    String gender;
}
