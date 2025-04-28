package dev.arack.twitterfake.iam.application.port.output.persistence;

import dev.arack.twitterfake.iam.domain.entities.RoleEntity;
import dev.arack.twitterfake.iam.domain.valueobject.RoleEnum;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface RolePersistence {
    Set<RoleEntity> findRoleEntitiesByRoleNameIn(List<RoleEnum> roleList);
    void saveAll(Set<RoleEntity> roleEntitySet);
    Optional<RoleEntity> findByRoleName(RoleEnum role);
}