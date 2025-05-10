package vn.edu.hcmaf.apigamestore.role;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;
import vn.edu.hcmaf.apigamestore.role.dto.RoleDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/role")
@Validated
//@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {
    @Autowired
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createRole(@RequestBody RoleDto role) {
        RoleEntity roleEntity = roleService.save(role.getName());
        return ResponseEntity.ok().body(new SuccessResponse<>("Tạo vai trò thành công", roleEntity));
    }
}
