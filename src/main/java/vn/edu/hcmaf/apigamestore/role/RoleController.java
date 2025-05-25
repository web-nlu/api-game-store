package vn.edu.hcmaf.apigamestore.role;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.role.dto.RoleDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/role")
@Validated
@RequiredArgsConstructor
/**
 * RoleController handles requests related to roles in the application.
 * It provides endpoints to retrieve all roles and create a new role.
 */
public class RoleController {

    private final RoleService roleService;

    /**
     * Retrieves all roles in the system.
     *
     * @return ResponseEntity containing a list of RoleEntity objects wrapped in a SuccessResponse.
     */
    @GetMapping("/all")
    @Operation(summary = "Get all roles", description = "Retrieve a list of all roles in the system.")
    public ResponseEntity<BaseResponse> getAllRoles() {
        List<RoleEntity> roles = roleService.getAllRoles();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get all roles successfully", roles));
    }
    /**
     * Creates a new role with the provided details.
     *
     * @param role RoleDto containing the details of the role to be created.
     * @return ResponseEntity containing the created RoleEntity wrapped in a SuccessResponse.
     */
    @PostMapping("/create")
    @Operation(summary = "Create a new role", description = "Create a new role with the provided details.")
    public ResponseEntity<BaseResponse> createRole(@RequestBody RoleDto role) {
        RoleEntity roleEntity = roleService.save(role.getName());
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Create role success", roleEntity));
    }
}
