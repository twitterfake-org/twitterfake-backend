package dev.arack.enlace.iam.application.internal.handler;

import dev.arack.enlace.iam.application.port.persistence.UserPersistence;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.iam.domain.entities.PermissionEntity;
import dev.arack.enlace.iam.domain.entities.RoleEntity;
import dev.arack.enlace.iam.domain.entities.UserDetailsEntity;
import dev.arack.enlace.iam.domain.events.UserCreatedEvent;
import dev.arack.enlace.iam.domain.valueobject.RoleEnum;
import dev.arack.enlace.iam.infrastructure.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class SeedingEventHandler {
    private final UserPersistence userPersistence;
    private final RoleJpaRepository rolePersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

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

        seedUsers(roleAdmin);

        log.info("Finished seeding roles and users for {} at {}", name, new Timestamp(System.currentTimeMillis()));
    }

    private void seedUsers(RoleEntity roleAdmin) {
        UserEntity user = UserEntity.builder()
                .username("string")
                .password(passwordEncoder.encode("string"))
                .userDetails(UserDetailsEntity.builder()
                        .enabled(true)
                        .accountNoExpired(true)
                        .credentialNoExpired(true)
                        .accountNoLocked(false)
                        .build())
                .roles(Set.of(roleAdmin))
                .build();

        userPersistence.save(user);
        eventPublisher.publishEvent(new UserCreatedEvent(this, user));
    }
}