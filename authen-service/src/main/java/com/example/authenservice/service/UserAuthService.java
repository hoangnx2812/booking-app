package com.example.authenservice.service;


import com.example.authenservice.dto.request.LoginUserRequest;
import com.example.authenservice.dto.request.RegisterUserRequest;
import com.example.authenservice.dto.request.UserInfoResponse;
import com.example.authenservice.dto.response.LoginUserResponse;
import com.example.commericalcommon.dto.request.IdRequest;

public interface UserAuthService {

    Object registerUser(RegisterUserRequest request);

    LoginUserResponse loginUser(LoginUserRequest request);

    UserInfoResponse getUserProfile();

    UserInfoResponse getAllUserProfiles();

    Object deleteUser(IdRequest request);

}
