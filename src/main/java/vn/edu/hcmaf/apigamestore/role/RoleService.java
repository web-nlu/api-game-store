package vn.edu.hcmaf.apigamestore.role;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Optional<RoleEntity>  findByName(String name) {
        return Optional.ofNullable(roleRepository.findByName(name).orElse(null));
    }
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public ResponseEntity<BaseResponse> getAllRoles() {
        return ResponseEntity.ok().body(new SuccessResponse<>("200", "Get all roles successfully", roleRepository.findAllByIsDeletedFalse()));
    }

    public ResponseEntity<BaseResponse> save(String role) {
        // Check if the role already exists
        if (roleRepository.existsByName(role)) {
            return ResponseEntity.status(400).body(new SuccessResponse<>("400", "Role already exists", null));
        }
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(role);
        return ResponseEntity.ok().body(new SuccessResponse<>("200", "Create role successfully", roleRepository.save(roleEntity)));
    }
    public  ResponseEntity<BaseResponse> deleteRole(Long id) {
        // Check if the role exists
        if (!roleRepository.existsById(id)) {
            return ResponseEntity.status(404).body(new SuccessResponse<>("404", "Role not found", null));
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<RoleEntity> roleEntity = roleRepository.findById(id);
        if (roleEntity.isPresent()) {
            roleEntity.get().setDeleted(true);
            roleEntity.get().setDeletedBy(username);
            roleEntity.get().setDeletedAt(String.valueOf(LocalDateTime.now()));
            roleRepository.save(roleEntity.get());
            return ResponseEntity.ok().body(new SuccessResponse<>("200", "Delete role successfully", null));
        } else {
            return ResponseEntity.status(404).body(new SuccessResponse<>("404", "Role not found", null));
        }
    }
}
