package com.example.authenservice.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KeyCloakConfig {
    KeycloakProperties keycloakProperties;

    @Bean(name = "adminKeycloak")
    public Keycloak adminKeycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getUrls())
                .realm(keycloakProperties.getAdmin().getRealm())
                .clientId(keycloakProperties.getAdmin().getClientId())
                .username(keycloakProperties.getAdmin().getUsername())
                .password(keycloakProperties.getAdmin().getPassword())
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

    @Bean(name = "appKeycloak")
    public Keycloak appKeycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getUrls())
                .realm(keycloakProperties.getRealm())
                .clientId(keycloakProperties.getClientId())
                .clientSecret(keycloakProperties.getClientSecret())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}
