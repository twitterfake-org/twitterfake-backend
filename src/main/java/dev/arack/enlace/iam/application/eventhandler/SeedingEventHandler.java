package dev.arack.enlace.iam.application.eventhandler;

import dev.arack.enlace.iam.application.port.output.persistence.UserPersistencePort;
import dev.arack.enlace.iam.domain.aggregate.UserEntity;
import dev.arack.enlace.iam.domain.entity.PermissionEntity;
import dev.arack.enlace.iam.domain.entity.RoleEntity;
import dev.arack.enlace.iam.domain.valueobject.RoleEnum;
import dev.arack.enlace.iam.infrastructure.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class SeedingEventHandler {
    private final UserPersistencePort userPersistencePort;
    private final RoleRepository rolePersistencePort;
    private final PasswordEncoder passwordEncoder;

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var name = event.getApplicationContext().getId();
        log.info("Starting to seed roles and users for {} at {}", name, new Timestamp(System.currentTimeMillis()));

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

        rolePersistencePort.saveAll(Set.of(roleAdmin, roleUser, roleGuest));

        UserEntity user = UserEntity.builder()
                .username("string")
                .password(passwordEncoder.encode("string"))
                .enabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .roles(Set.of(roleAdmin))
                .build();

        userPersistencePort.save(user);

        log.info("Finished seeding roles and users for {} at {}", name, new Timestamp(System.currentTimeMillis()));
    }
}