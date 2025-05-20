package vn.edu.hcmaf.apigamestore.role.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

}
