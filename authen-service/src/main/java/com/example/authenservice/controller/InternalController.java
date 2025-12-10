package com.example.authenservice.controller;

import com.example.authenservice.service.UserService;
import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.utils.MessageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InternalController {
    UserService userService;
    MessageUtil messageUtil;

    @GetMapping("/users/{id}")
    public BaseResponse<Object> getUserById(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        return BaseResponse.builder()
                .resultDesc(messageUtil.getMessage("FETCH_SUCCESS"))
                .data(userService.getUserById(id))
                .cost(System.currentTimeMillis() - startTime)
                .build();
    }
}
