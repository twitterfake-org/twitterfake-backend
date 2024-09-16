package dev.arack.enlace;

import dev.arack.enlace.iam.domain.model.PermissionEntity;
import dev.arack.enlace.iam.domain.model.RoleEntity;
import dev.arack.enlace.iam.domain.model.RoleEnum;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.IUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@EnableJpaAuditing
@SpringBootApplication
@Log4j2
public class EnlaceBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnlaceBackendApplication.class, args);

        String url = "http://localhost:8080/swagger-ui/index.html";
        log.info("\n\n• Swagger UI is available at » " + url + "\n");
    }

    @Bean
    CommandLineRunner init(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            /* Create PERMISSIONS */
            PermissionEntity createPermission = PermissionEntity.builder().name("CREATE_TEST").build();
            PermissionEntity readPermission = PermissionEntity.builder().name("READ_TEST").build();
            PermissionEntity updatePermission = PermissionEntity.builder().name("UPDATE_TEST").build();
            PermissionEntity deletePermission = PermissionEntity.builder().name("DELETE_TEST").build();
//            PermissionEntity refactorPermission = PermissionEntity.builder().name("REFACTOR").build();

            /* Create ROLES */
            RoleEntity roleAdmin = RoleEntity.builder().roleName(RoleEnum.ADMIN).permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission)).build();
//            RoleEntity roleUser = RoleEntity.builder().roleName(RoleEnum.USER).permissionList(Set.of(createPermission, readPermission)).build();
//            RoleEntity roleInvited = RoleEntity.builder().roleName(RoleEnum.INVITED).permissionList(Set.of(readPermission)).build();
//            RoleEntity roleDeveloper = RoleEntity.builder().roleName(RoleEnum.DEVELOPER).permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission)).build();

            /* CREATE USERS */
            UserEntity user = UserEntity.builder()
                    .username("string")
                    .password(passwordEncoder.encode("string"))
                    .isEnabled(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(roleAdmin))
                    .build();

            userRepository.save(user);
        };
    }
}
