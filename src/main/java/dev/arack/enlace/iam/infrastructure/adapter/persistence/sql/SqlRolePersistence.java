package dev.arack.enlace.iam.infrastructure.adapter.persistence.sql;

import dev.arack.enlace.iam.application.port.output.persistence.RolePersistence;
import dev.arack.enlace.iam.domain.entities.RoleEntity;
import dev.arack.enlace.iam.domain.valueobject.RoleEnum;
import dev.arack.enlace.iam.infrastructure.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SqlRolePersistence implements RolePersistence {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public Set<RoleEntity> findRoleEntitiesByRoleNameIn(List<RoleEnum> roleList) {
        return roleJpaRepository.findRoleEntitiesByRoleNameIn(roleList);
    }

    @Override
    public void saveAll(Set<RoleEntity> roleEntitySet) {
        roleJpaRepository.saveAll(roleEntitySet);
    }
}