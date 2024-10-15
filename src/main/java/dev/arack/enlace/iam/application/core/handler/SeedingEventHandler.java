package dev.arack.enlace.iam.application.core.handler;

import dev.arack.enlace.iam.application.port.output.persistence.RolePersistence;
import dev.arack.enlace.iam.application.port.output.persistence.UserPersistence;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.iam.domain.entities.PermissionEntity;
import dev.arack.enlace.iam.domain.entities.RoleEntity;
import dev.arack.enlace.iam.domain.entities.UserDetailsEntity;
import dev.arack.enlace.iam.domain.events.UserCreatedEvent;
import dev.arack.enlace.iam.domain.valueobject.RoleEnum;
import dev.arack.enlace.iam.infrastructure.repository.jpa.JpaRoleRepository;
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
    private final RolePersistence rolePersistence;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var name = event.getApplicationContext().getId();
        log.info("Starting to seed roles and users for {} at {}", name, new Timestamp(System.currentTimeMillis()));

        PermissionEntity createPermission = PermissionEntity.builder().name("CREATE").build();
        PermissionEntity readPermission = PermissionEntity.builder().name("READ").build();
        PermissionEntity updatePermission = PermissionEntity.builder().name("UPDATE").build();
        PermissionEntity deletePermission = PermissionEntity.builder().name("DELETE").build();

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

        rolePersistence.saveAll(Set.of(roleAdmin, roleUser, roleGuest));

        //USUARIO DE PRUEBA
        seedUsers(roleAdmin);

        log.info("Finished seeding roles and users for {} at {}", name, new Timestamp(System.currentTimeMillis()));
    }

    private void seedUsers(RoleEntity roleAdmin) {

        // This is the user that will be created during the seeding process
        UserEntity user = UserEntity.builder()
                .username("string")
                .password(passwordEncoder.encode("string"))
                .roles(Set.of(roleAdmin))
                .userDetails(UserDetailsEntity.builder()
                        .enabled(true)
                        .accountNoExpired(true)
                        .credentialNoExpired(true)
                        .accountNoLocked(false)
                        .build())
                .build();

        userPersistence.save(user);
        eventPublisher.publishEvent(new UserCreatedEvent(this, user, "string", "string"));
    }
}