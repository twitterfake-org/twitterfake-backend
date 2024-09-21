package dev.arack.enlace.iam.infrastructure.adapter.persistence;

import dev.arack.enlace.iam.application.port.output.persistence.RolePersistencePort;
import dev.arack.enlace.iam.domain.entity.RoleEntity;
import dev.arack.enlace.iam.domain.valueobject.RoleEnum;
import dev.arack.enlace.iam.infrastructure.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RolePersistenceAdapter implements RolePersistencePort {

    private final RoleRepository roleRepository;

    @Override
    public Set<RoleEntity> findRoleEntitiesByRoleNameIn(List<RoleEnum> roleList) {
        return roleRepository.findRoleEntitiesByRoleNameIn(roleList);
    }

    @Override
    public void saveAll(Set<RoleEntity> roleEntitySet) {
        roleRepository.saveAll(roleEntitySet);
    }
}