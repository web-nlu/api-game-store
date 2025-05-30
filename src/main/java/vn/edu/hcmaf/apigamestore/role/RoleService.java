package vn.edu.hcmaf.apigamestore.role;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;


  public RoleEntity getByName(String name) throws NullPointerException {
    return roleRepository.findByName(name).orElse(null);
  }

    public RoleEntity findByName(String name) throws NullPointerException {
        return roleRepository.findByName(name).orElseThrow(() -> new NullPointerException(name));
    }
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public List<RoleEntity> getAllRoles() {
        return  roleRepository.findAllByIsDeletedFalse();
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
      roleEntity.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
      roleRepository.save(roleEntity);
      return true;
    }
}
