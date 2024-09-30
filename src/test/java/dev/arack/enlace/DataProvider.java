package dev.arack.enlace;

import ch.qos.logback.core.encoder.EchoEncoder;
import dev.arack.enlace.iam.application.dto.request.LoginRequest;
import dev.arack.enlace.iam.application.dto.request.SignupRequest;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.iam.domain.entities.PermissionEntity;
import dev.arack.enlace.iam.domain.entities.RoleEntity;
import dev.arack.enlace.iam.domain.valueobject.RoleEnum;

import java.util.Arrays;
import java.util.Set;

public class DataProvider {

    public static Set<RoleEntity> roleEntityMock() {
        PermissionEntity createPermission = PermissionEntity.builder().name("CREATE_TEST").build();
        PermissionEntity readPermission = PermissionEntity.builder().name("READ_TEST").build();
        PermissionEntity updatePermission = PermissionEntity.builder().name("UPDATE_TEST").build();
        PermissionEntity deletePermission = PermissionEntity.builder().name("DELETE_TEST").build();

        RoleEntity roleAdmin = RoleEntity.builder()
                .roleName(RoleEnum.ADMIN)
                .permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
                .build();

        RoleEntity roleUser = RoleEntity.builder()
                .roleName(RoleEnum.USER)
                .permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
                .build();

        RoleEntity roleGuest = RoleEntity.builder()
                .roleName(RoleEnum.GUEST)
                .permissionList(Set.of(readPermission))
                .build();
        return Set.of(roleUser);
    }

    public static UserEntity userEntityMock() {
        return UserEntity.builder()
                .username("username")
                .password("encodedPassword")
                .roles(roleEntityMock())
                .enabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();
    }
}