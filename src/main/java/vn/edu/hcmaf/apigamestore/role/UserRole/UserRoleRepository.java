package vn.edu.hcmaf.apigamestore.role.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;
import vn.edu.hcmaf.apigamestore.user.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    Optional<UserRoleEntity> findByUserAndRole(UserEntity userEntity, RoleEntity roleEntity);

    List<UserRoleEntity> findAllByUser(UserEntity userEntity);

    List<UserRoleEntity> findAllByUserAndIsDeletedFalse(UserEntity userEntity);
}
