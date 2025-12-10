package com.example.postservice.repository.httpclient;

import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.response.user.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authentication-service", url = "${app.services.authentication.url}")
public interface AuthenticationClient {
    @GetMapping(value = "/internal/users/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    BaseResponse<UserInfoResponse> getUserById(@PathVariable String id);
}
