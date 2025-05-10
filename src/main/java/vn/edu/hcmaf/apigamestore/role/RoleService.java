package vn.edu.hcmaf.apigamestore.role;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public RoleEntity findByName(String name) throws NullPointerException {
        return roleRepository.findByName(name).orElseThrow(() -> new NullPointerException(name));
    }
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public ResponseEntity<BaseResponse> getAllRoles() {
        return ResponseEntity.ok().body(new SuccessResponse<>("Get all roles successfully", roleRepository.findAllByIsDeletedFalse()));
    }

    public RoleEntity save(String role) {
        // Check if the role already exists
        if (roleRepository.existsByName(role)) {
            throw new IllegalArgumentException("Role already exists");
        }
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(role);
        return roleRepository.save(roleEntity);
    }
    public boolean deleteRole(Long id) {
        // Check if the role exists
      RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(() -> new NullPointerException("Role does not exist"));

      String username = SecurityContextHolder.getContext().getAuthentication().getName();

      roleEntity.setDeleted(true);
      roleEntity.setDeletedBy(username);
      roleEntity.setDeletedAt(String.valueOf(LocalDateTime.now()));
      roleRepository.save(roleEntity);
      return true;
    }
}
