package dev.arack.twitterfake.iam.infrastructure.repository;

import dev.arack.twitterfake.iam.domain.model.entities.RoleEntity;
import dev.arack.twitterfake.iam.domain.model.valueobject.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Set<RoleEntity> findRoleEntitiesByRoleNameIn(List<RoleEnum> roleList);
    Optional<RoleEntity> findByRoleName(RoleEnum roleEnum);
}