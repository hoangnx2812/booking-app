package com.example.authenservice.configuration;

import com.example.authenservice.entity.*;
import com.example.authenservice.repository.*;
import com.example.commericalcommon.exception.GlobalException;
import com.example.commericalcommon.exception.ErrorCode;
import com.example.commericalcommon.utils.Constant;
import com.example.commericalcommon.utils.PermissionConstant;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.example.commericalcommon.utils.Constant.PrefixNo.USER_NO;
import static com.example.commericalcommon.utils.Util.*;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    @NonFinal
    @Value("${app.admin.account.username}")
    String ADMIN_USER_NAME;

    @NonFinal
    @Value("${app.admin.account.password}")
    String ADMIN_PASSWORD;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "org.postgresql.Driver")
    ApplicationRunner applicationRunner(UserAuthRepository userAuthRepository,
                                        UserInfoRepository userInfoRepository,
                                        RoleRepository roleRepository,
                                        PermissionRepository permissionRepository,
                                        UserRoleRepository userRoleRepository,
                                        RolePermissionRepository rolePermissionRepository,
                                        @Qualifier("adminKeycloak")
                                        Keycloak adminKeycloak,
                                        KeycloakProperties keycloakProperties) {
        return args -> {
            log.info("Initializing application.....");
            if (roleRepository.findByName(Constant.DefaultRole.ADMIN).isEmpty()) {
                //Tao role mac dinh trong db
                List<Role> defaultRoles = List.of(
                        Role.builder()
                                .description("Administrator role with full permissions")
                                .name(Constant.DefaultRole.ADMIN)
                                .build(),
                        Role.builder()
                                .description("General user role with limited permissions")
                                .name(Constant.DefaultRole.USER)
                                .build(),
                        Role.builder()
                                .description("Artist role with permissions to manage own content")
                                .name(Constant.DefaultRole.ARTIST)
                                .build()
                );

                //Lay danh sach role chua co trong db
                List<Role> rolesToSave = defaultRoles.stream()
                        .filter(role -> !roleRepository.existsByName(role.getName()))
                        .toList();

                if (!rolesToSave.isEmpty()) {
                    roleRepository.saveAll(rolesToSave);
                }

                //Tao role trong keycloak
                var rolesResource = adminKeycloak.realm(keycloakProperties.getRealm()).roles();

                //Lay danh sach role chua co trong keycloak
                var existing = rolesResource.list()
                        .stream()
                        .map(RoleRepresentation::getName)
                        .toList();

                //Tao role mac dinh trong keycloak
                defaultRoles.stream()
                        .filter(role -> !existing.contains(role.getName()))
                        .forEach(role ->
                                rolesResource.create(new RoleRepresentation(role.getName(),
                                        role.getDescription(),
                                        false))
                        );


                //Tao permission mac dinh trong db
                List<String> allPermissions = Stream.of(
                        // AUTH
                        PermissionConstant.Auth.DELETE_ACCOUNT_INIT,
                        PermissionConstant.Auth.DELETE_ACCOUNT_CONFIRM,
                        PermissionConstant.Auth.REGISTER_ARTIST,
                        PermissionConstant.Auth.REGISTER_DEVICE,
                        PermissionConstant.Auth.REGISTER_BIOMETRIC,

                        // USER PROFILE
                        PermissionConstant.UserProfile.VIEW,
                        PermissionConstant.UserProfile.UPDATE,

                        // POST
                        PermissionConstant.Post.LIST_VIEW,
                        PermissionConstant.Post.DETAIL_VIEW,
                        PermissionConstant.Post.RATE_VIEW,
                        PermissionConstant.Post.RATE_CREATE,
                        PermissionConstant.Post.FAVORITE_TOGGLE,
                        PermissionConstant.Post.BLOCK,
                        PermissionConstant.Post.REPORT,
                        PermissionConstant.Post.SHARE,
                        PermissionConstant.Post.FAVORITE_LIST_VIEW,

                        // MESSAGE
                        PermissionConstant.Message.HISTORY_VIEW,
                        PermissionConstant.Message.MARK_ALL_READ,
                        PermissionConstant.Message.SEND,

                        // ARTIST
                        PermissionConstant.Artist.LIST_VIEW,
                        PermissionConstant.Artist.PROFILE_VIEW,
                        PermissionConstant.Artist.BLOCK,
                        PermissionConstant.Artist.REPORT,
                        PermissionConstant.Artist.SHARE,
                        PermissionConstant.Artist.POST_LIST_VIEW,
                        PermissionConstant.Artist.SERVICE_LIST_VIEW,
                        PermissionConstant.Artist.SERVICE_DETAIL_VIEW,
                        PermissionConstant.Artist.REVIEW_LIST_VIEW,
                        PermissionConstant.Artist.REVIEW_CREATE,
                        PermissionConstant.Artist.POST_CREATE,
                        PermissionConstant.Artist.POST_UPDATE,
                        PermissionConstant.Artist.POST_DELETE,
                        PermissionConstant.Artist.SERVICE_CREATE,
                        PermissionConstant.Artist.SERVICE_UPDATE,
                        PermissionConstant.Artist.SERVICE_DELETE,
                        PermissionConstant.Artist.PROMOTION_CREATE,
                        PermissionConstant.Artist.PROMOTION_UPDATE,
                        PermissionConstant.Artist.PROMOTION_DELETE,
                        PermissionConstant.Artist.PROFILE_UPDATE,

                        // BOOKING
                        PermissionConstant.Booking.USER_HISTORY_VIEW,
                        PermissionConstant.Booking.USER_CREATE,
                        PermissionConstant.Booking.ARTIST_LIST_VIEW,

                        // NOTIFICATION
                        PermissionConstant.Notification.USER_HISTORY_VIEW,
                        PermissionConstant.Notification.BOOKING_ARTIST_VIEW,

                        // ADMIN
                        PermissionConstant.Admin.REPORTED_POST_LIST_VIEW,
                        PermissionConstant.Admin.REPORTED_ARTIST_LIST_VIEW,
                        PermissionConstant.Admin.REPORTED_POST_DELETE,
                        PermissionConstant.Admin.REPORTED_ARTIST_LOCK,
                        PermissionConstant.Admin.ARTIST_REGISTRATION_LIST_VIEW,
                        PermissionConstant.Admin.ARTIST_REGISTRATION_APPROVE,
                        PermissionConstant.Admin.ARTIST_REGISTRATION_REJECT
                ).toList();

                // Lay danh sach permission chua co trong db
                List<Permission> newPermissions = allPermissions.stream()
                        .filter(p -> !permissionRepository.existsByName(p))
                        .map(p -> {
                            Permission permission = new Permission();
                            permission.setName(p);
                            permission.setDescription(p.replace("_", " ").toLowerCase());
                            return permission;
                        })
                        .toList();

                // Them permission moi vao db
                if (!newPermissions.isEmpty()) {
                    permissionRepository.saveAll(newPermissions);
                }

                Role adminRole = roleRepository.findByName(Constant.DefaultRole.ADMIN)
                        .orElseThrow(() -> new GlobalException(ErrorCode.ROLE_NOT_EXISTED));

                List<Permission> allPermissionEntities = permissionRepository.findAll();

                // Lay danh sach permission da duoc gan voi role admin
                List<RolePermission> newRolePermissions = allPermissionEntities.stream()
                        .filter(permission -> {
                            RolePermissionId id = new RolePermissionId();
                            id.setRoleId(adminRole.getId());
                            id.setPermissionsName(permission.getName());
                            return !rolePermissionRepository.existsById(id);
                        })
                        .map(permission -> {
                            RolePermissionId id = new RolePermissionId();
                            id.setRoleId(adminRole.getId());
                            id.setPermissionsName(permission.getName());

                            RolePermission rp = new RolePermission();
                            rp.setId(id);
                            rp.setRole(adminRole);
                            rp.setPermissionsName(permission);
                            return rp;
                        })
                        .toList();

                if (!newRolePermissions.isEmpty()) {
                    rolePermissionRepository.saveAll(newRolePermissions);
                }

                // Tao permission role trong keycloak
                allPermissions.stream()
                        .filter(permission -> !existing.contains(permission))
                        .forEach(permission ->
                                rolesResource.create(new RoleRepresentation(permission,
                                        "Permission role",
                                        false))
                        );

                List<RoleRepresentation> permissionRoles = allPermissions.stream()
                        .map(rolesResource::get)
                        .map(roleRes -> {
                            try {
                                return roleRes.toRepresentation();
                            } catch (Exception e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList();

                rolesResource.get(Constant.DefaultRole.ADMIN).addComposites(permissionRoles);

            }

            if (userAuthRepository.findByUserName(ADMIN_USER_NAME).isEmpty()) {
                //Tao user admin mac dinh trong db
                String salt = generateSalt(null);
                String encryptedPwd = encryptSHA256(ADMIN_PASSWORD);
                String hashedPwd = encryptSHA256(encryptedPwd + salt);
                UserAuth user = new UserAuth();
                user.setUserName(ADMIN_USER_NAME);
                user.setUserPwdHash(hashedPwd);
                user.setUserSalt(salt);
                userAuthRepository.save(user);

                //Tao user info cho admin
                UserInfo userInfo = new UserInfo();
                userInfo.setFirstName("Admin");
                userInfo.setMiddleName("System");
                userInfo.setLastName("Administrator");
                userInfo.setFullName("Admin System Administrator");
                userInfo.setUserNo(generateNo(USER_NO));
                userInfo.setUserAuth(user);
                userInfoRepository.save(userInfo);

                //Tao user role cho admin
                UserRole userRole = new UserRole();
                Role adminRole = roleRepository.findByName(Constant.DefaultRole.ADMIN)
                        .orElseThrow(() -> new GlobalException(ErrorCode.ROLE_NOT_EXISTED));
                UserRoleId userRoleId = new UserRoleId();
                userRoleId.setRoleId(adminRole.getId());
                userRoleId.setUserAuthId(user.getId());
                userRole.setId(userRoleId);
                userRole.setUserAuth(user);
                userRole.setRole(adminRole);
                userRoleRepository.save(userRole);

                //Tao user admin mac dinh trong keycloak
                UserRepresentation userRepresentation = new UserRepresentation();
                userRepresentation.setUsername(ADMIN_USER_NAME);
                userRepresentation.setEmail("adminbka@yopmail.com");
                userRepresentation.setFirstName("Admin");
                userRepresentation.setLastName("Administrator");
                userRepresentation.setEmailVerified(true);
                userRepresentation.setEnabled(true);
                userRepresentation.setRequiredActions(Collections.emptyList());

                try (Response response = adminKeycloak.realm(keycloakProperties.getRealm())
                        .users()
                        .create(userRepresentation)) {
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
                        credential.setValue(hashedPwd);
                        adminKeycloak.realm(keycloakProperties.getRealm())
                                .users()
                                .get(userId)
                                .resetPassword(credential);

                        //Them role mac dinh la USER
                        UserResource userResource = adminKeycloak
                                .realm(keycloakProperties.getRealm())
                                .users()
                                .get(userId);
                        RoleRepresentation generalRole = adminKeycloak
                                .realm(keycloakProperties.getRealm())
                                .roles()
                                .get(Constant.DefaultRole.ADMIN)
                                .toRepresentation();
                        userResource.roles().realmLevel().add(Collections.singletonList(generalRole));

                        log.info("User created in Keycloak: {}", ADMIN_USER_NAME);
                    } else {
                        String errorBody = response.readEntity(String.class);
                        log.error("Failed to create user in Keycloak: {}", errorBody);
                        throw new GlobalException(ErrorCode.INVALID_INPUT);
                    }
                }
            }
            log.info("Application initialization completed .....");
        };
    }
}
