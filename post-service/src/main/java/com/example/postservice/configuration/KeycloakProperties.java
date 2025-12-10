package com.example.postservice.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class KeycloakProperties {
    String realm;
    String domain;
    String clientId;
    String clientSecret;
    String urls;
    Admin admin;

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Admin {
        String clientId;
        String realm;
        String username;
        String password;
    }


}
