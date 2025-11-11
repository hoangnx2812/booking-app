package com.example.authenservice.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserInfoResponse {
    Integer id;
    String username;
    String email;
    String fullName;
    String birthDate;
    String phoneNumber;
}
