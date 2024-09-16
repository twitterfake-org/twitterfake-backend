package dev.arack.enlace.iam.application.ports.output;

import dev.arack.enlace.iam.domain.model.RoleEntity;
import dev.arack.enlace.iam.domain.model.RoleEnum;

import java.util.List;
import java.util.Set;


public interface IRolePersistencePort {
    Set<RoleEntity> findRoleEntitiesByRoleNameIn(List<RoleEnum> roleList);
}