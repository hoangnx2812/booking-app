package com.example.authenservice.service.impl;

import com.example.authenservice.dto.response.UserInfoResponse;
import com.example.authenservice.entity.UserInfo;
import com.example.authenservice.repository.UserInfoRepository;
import com.example.authenservice.service.UserService;
import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.exception.ErrorCode;
import com.example.commericalcommon.exception.GlobalException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    UserInfoRepository userInfoRepository;

    @Override
    public Object getUserInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        UserInfo userInfo = userInfoRepository.findByUserAuth_UserName(name)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_EXISTED));

        return UserInfoResponse.builder()
                .userNo(userInfo.getUserNo())
                .fullName(userInfo.getFullName())
                .email(userInfo.getEmail())
                .phoneNumber(userInfo.getPhone())
                .areaId(userInfo.getArea().getId())
                .addressDetail(userInfo.getFullAddress())
                .avatar(userInfo.getAvatar())
                .gender(userInfo.getGender())
                .build();
    }
}
