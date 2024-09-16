package dev.arack.enlace.iam.infrastructure.adapters.output.repositories;

import dev.arack.enlace.iam.domain.model.RoleEntity;
import dev.arack.enlace.iam.domain.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    Set<RoleEntity> findRoleEntitiesByRoleNameIn(List<RoleEnum> roleList);
}