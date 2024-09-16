package dev.arack.enlace.iam.infrastructure.adapters.output;

import dev.arack.enlace.iam.application.ports.output.IRolePersistencePort;
import dev.arack.enlace.iam.domain.model.RoleEntity;
import dev.arack.enlace.iam.domain.model.RoleEnum;
import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RolePersistenceAdapter implements IRolePersistencePort {

    private final IRoleRepository roleRepository;

    @Override
    public Set<RoleEntity> findRoleEntitiesByRoleNameIn(List<RoleEnum> roleList) {
        return roleRepository.findRoleEntitiesByRoleNameIn(roleList);
    }
}