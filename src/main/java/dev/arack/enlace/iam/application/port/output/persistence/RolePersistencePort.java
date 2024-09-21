package dev.arack.enlace.iam.application.port.output.persistence;

import dev.arack.enlace.iam.domain.entity.RoleEntity;
import dev.arack.enlace.iam.domain.valueobject.RoleEnum;

import java.util.List;
import java.util.Set;


public interface RolePersistencePort {
    Set<RoleEntity> findRoleEntitiesByRoleNameIn(List<RoleEnum> roleList);
    void saveAll(Set<RoleEntity> roleEntitySet);
}