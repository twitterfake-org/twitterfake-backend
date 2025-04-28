package dev.arack.twitterfake.iam.infrastructure.repository.jpa;

import dev.arack.twitterfake.iam.domain.entities.RoleEntity;
import dev.arack.twitterfake.iam.domain.valueobject.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface JpaRoleRepository extends JpaRepository<RoleEntity, Long> {

    Set<RoleEntity> findRoleEntitiesByRoleNameIn(List<RoleEnum> roleList);
    Optional<RoleEntity> findByRoleName(RoleEnum roleEnum);
}