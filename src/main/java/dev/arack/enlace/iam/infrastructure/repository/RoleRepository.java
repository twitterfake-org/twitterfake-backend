package dev.arack.enlace.iam.infrastructure.repository;

import dev.arack.enlace.iam.domain.entity.RoleEntity;
import dev.arack.enlace.iam.domain.valueobject.RoleEnum;
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