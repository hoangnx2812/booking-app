package com.example.authenservice.service.impl;


import com.example.authenservice.configuration.KeycloakProperties;
import com.example.authenservice.dto.request.LoginUserRequest;
import com.example.authenservice.dto.request.RegisterUserRequest;
import com.example.authenservice.dto.request.UserInfoResponse;
import com.example.authenservice.dto.response.LoginUserResponse;
import com.example.authenservice.entity.UserAuth;
import com.example.authenservice.entity.UserInfo;
import com.example.authenservice.repository.UserAuthRepository;
import com.example.authenservice.repository.UserInfoRepository;
import com.example.authenservice.service.UserAuthService;
import com.example.commericalcommon.dto.request.IdRequest;
import com.example.commericalcommon.exception.AppException;
import com.example.commericalcommon.exception.ErrorCode;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static com.example.commericalcommon.utils.Constant.PrefixNo.USER_NO;
import static com.example.commericalcommon.utils.Util.generateNo;
import static com.example.commericalcommon.utils.Util.generateSalt;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {
    KeycloakProperties keycloakProperties;
    UserAuthRepository userAuthRepository;
    PasswordEncoder passwordEncoder;
    UserInfoRepository userInfoRepository;

    @Override
    @Transactional
    public Object registerUser(RegisterUserRequest request) {
        UserAuth user = new UserAuth();
        user.setUserName(request.getUsername());
        user.setUserPwdHash(passwordEncoder.encode(request.getPassword()));
        user.setUserSalt(generateSalt(null));
        userAuthRepository.save(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName(request.getFirstName());
        userInfo.setMiddleName(request.getMiddleName());
        userInfo.setLastName(request.getLastName());
        userInfo.setFullName(request.getFirstName() + " " + request.getMiddleName() + " " + request.getLastName());
        userInfo.setBirthday(request.getBirthDate());
        userInfo.setEmail(request.getEmail());
        userInfo.setPhone(request.getUsername());
        userInfo.setUserNo(generateNo(USER_NO));
        userInfo.setUserAuth(user);
        userInfoRepository.save(userInfo);

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getUrls())
                .realm(keycloakProperties.getAdmin().getRealm())
                .clientId(keycloakProperties.getAdmin().getClientId())
                .username(keycloakProperties.getAdmin().getUsername())
                .password(keycloakProperties.getAdmin().getPassword())
                .build();


        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(request.getUsername());
        userRepresentation.setEmail(request.getEmail());
        userRepresentation.setFirstName(request.getFirstName());
        userRepresentation.setLastName(request.getLastName());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        userRepresentation.setRequiredActions(Collections.emptyList());

        Response response = keycloak.realm(keycloakProperties.getRealm())
                .users()
                .create(userRepresentation);

        if (HttpStatus.CREATED.value() == response.getStatus()) {
            String location = response.getLocation().toString();
            String userId = location.substring(location.lastIndexOf("/") + 1);

            user.setUserInfoNo(userInfo.getUserNo());
            user.setUserInfoId(userInfo.getId());
            user.setKeycloakId(userId);
            userAuthRepository.save(user);

            //Them pass
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(request.getPassword());
            keycloak.realm(keycloakProperties.getRealm())
                    .users()
                    .get(userId)
                    .resetPassword(credential);

            //Them role mac dinh la general
            UserResource userResource = keycloak
                    .realm(keycloakProperties.getRealm())
                    .users()
                    .get(userId);
            RoleRepresentation generalRole = keycloak
                    .realm(keycloakProperties.getRealm())
                    .roles()
                    .get("general")
                    .toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(generalRole));

            log.info("User created in Keycloak: {}", request.getUsername());
        } else {
            String errorBody = response.readEntity(String.class);
            log.error("Failed to create user in Keycloak: {}", errorBody);
            keycloak.close();
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        keycloak.close();
        return "User registered successfully";
    }


    @Override
    public LoginUserResponse loginUser(LoginUserRequest request) {
        UserAuth user = userAuthRepository
                .findByUserName(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getUserPwdHash());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        try (Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getUrls())
                .realm(keycloakProperties.getRealm())
                .clientId(keycloakProperties.getAdminClientId())
                .clientSecret(keycloakProperties.getAdminClientSecret())
                .username(request.getUsername())
                .password(request.getPassword())
                .grantType(OAuth2Constants.PASSWORD)
                .build()) {

            AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();

            return LoginUserResponse.builder()
                    .accessToken(tokenResponse.getToken())
                    .refreshToken(tokenResponse.getRefreshToken())
                    .build();

        } catch (jakarta.ws.rs.WebApplicationException ex) {
            log.error("HTTP Error: {}", ex.getResponse().getStatus());
            String body = ex.getResponse().readEntity(String.class);
            log.error("Response body: {}", body);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, body);

        } catch (Exception e) {
            log.error("Login failed cause: ", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public UserInfoResponse getUserProfile() {
        return null;
    }

    @Override
    public UserInfoResponse getAllUserProfiles() {
        return null;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('admin')")
    public Object deleteUser(IdRequest request) {
        UserInfo userInfo = userInfoRepository.findByUserAuthId(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String keyCloakId = userInfo.getUserAuth().getKeycloakId();
        userInfoRepository.delete(userInfo);
        userAuthRepository.deleteById(request.getId());


        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getUrls())
                .realm(keycloakProperties.getAdmin().getRealm())
                .clientId(keycloakProperties.getAdmin().getClientId())
                .username(keycloakProperties.getAdmin().getUsername())
                .password(keycloakProperties.getAdmin().getPassword())
                .build();

        Response response = keycloak.realm(keycloakProperties.getRealm())
                .users()
                .delete(keyCloakId);

        keycloak.close();
        if (HttpStatus.NO_CONTENT.value() != response.getStatus()) {
            String errorBody = response.readEntity(String.class);
            log.error("Failed to deleted user in Keycloak: {}", errorBody);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        return "User deleted";
    }
}

