package vn.edu.hcmaf.apigamestore.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);

    boolean existsByName(String role);

    List<RoleEntity> findAllByIsDeletedFalse();

    List<RoleEntity> findAllByIdInAndIsDeletedFalse(Set<Long> ids);
}
