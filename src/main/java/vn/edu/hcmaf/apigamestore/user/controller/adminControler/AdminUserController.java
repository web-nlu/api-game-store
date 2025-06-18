package vn.edu.hcmaf.apigamestore.user.controller.adminControler;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.auth.AuthService;
import vn.edu.hcmaf.apigamestore.auth.dto.LoginResponseDto;
import vn.edu.hcmaf.apigamestore.auth.dto.request.RegisterRequestDto;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingResponseDto;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;
import vn.edu.hcmaf.apigamestore.role.RoleService;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;
import vn.edu.hcmaf.apigamestore.user.dto.FilterUserRequestDTO;
import vn.edu.hcmaf.apigamestore.user.dto.UpdateRoleUserDto;
import vn.edu.hcmaf.apigamestore.user.dto.UpdateUserDto;
import vn.edu.hcmaf.apigamestore.user.dto.UserResponseDto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@Validated
/**
 * AdminUserController handles requests related to user management for administrators.
 * It provides endpoints to retrieve user information, including lazy loading support.
 */
public class AdminUserController {
    private final UserService userService;
    private final AuthService authService;
    private final RoleService roleService;

    /**
     * Retrieves all users in the system.
     *
     * @return ResponseEntity containing a list of UserEntity objects wrapped in a SuccessResponse.
     */
    @GetMapping()
    @Operation(summary = "Filter users", description = "Retrieve a list of all users in the system.")
    public ResponseEntity<BaseResponse> getAllUsers(@ModelAttribute @Valid FilterUserRequestDTO requestDTO) {
      LazyLoadingResponseDto<List<UserResponseDto>> users = userService.filter(requestDTO);
      return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get all User info success", users));
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> create(@RequestBody @Valid RegisterRequestDto requestDTO) {
      RoleEntity roleEntity = roleService.getByName("STAFF");
      if (roleEntity == null) {
        roleEntity = roleService.save("STAFF");
      }

      UserEntity userEntity = userService.create(requestDTO, roleEntity);
      return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Reregister success", userService.toUserResponseDto(userEntity)));
    }

    /**
     * Retrieves user information by user ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return ResponseEntity containing the UserEntity wrapped in a SuccessResponse.
     */
    @GetMapping("/id/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieve user information by user ID.")
    public ResponseEntity<BaseResponse> getUserById(@PathVariable Long userId) {
        UserEntity userEntity = userService.getUserById(userId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get User Id : " + userId + " info success", userEntity));
    }



    /**
     * Retrieves user information by username (email).
     *
     * @param username The username (email) of the user to retrieve.
     * @return ResponseEntity containing the UserEntity wrapped in a SuccessResponse.
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieve user information by username (email).")
    public ResponseEntity<BaseResponse> getUserByUsername(@PathVariable String username) {
        UserEntity userEntity = userService.getUserByEmail(username);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get User Name : " + username + " info success", userEntity));
    }

    /**
     * Updates user information by user ID.
     * <p>
     * if user already has the role, and it is active, do not add it into updateRoleUserDto, because it is already active, and this method will not add it again.
     *
     * @param updateRoleUserDto UpdateRoleUserDto containing the details to update.
     * @param userId            The ID of the user to update.
     * @return ResponseEntity containing the updated UserEntity wrapped in a SuccessResponse.
     */
    @PutMapping("id/{userId}/update-role")
    @Operation(summary = "Update user role", description = "Update the role of a user by user ID.")
    public ResponseEntity<BaseResponse> updateUserRole(@RequestBody UpdateRoleUserDto updateRoleUserDto, @PathVariable Long userId) {
        UserEntity userEntity = userService.updateRolesUser(updateRoleUserDto.getRoleUpdateMap(), userId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Update User Id: " + userId, userEntity.getActiveRoles()));
    }
    @Operation(summary = "Register", description = "Register a new Staff")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/register-staff")
    public ResponseEntity<BaseResponse> registerStaff(@RequestBody @Valid RegisterRequestDto request) {
        RoleEntity roleEntity = roleService.getByName("STAFF");
        if (roleEntity == null) {
            roleEntity = roleService.save("STAFF");
        }
         authService.register(request, roleEntity);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Reregister success", null));
    }
    @PutMapping("/id/{userId}/reset-pass")
    @Operation(summary = "Reset password", description = "Reset the password of a user by user ID.")
    public ResponseEntity<BaseResponse> resetPassword(@PathVariable Long userId) {
        UserEntity userEntity = userService.getUserById(userId);
        if (userEntity == null) {
            return ResponseEntity.badRequest().body(new SuccessResponse<>("ERROR", "User not found with ID: " + userId, null));
        }
        authService.resetPassword(userEntity);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Reset password for User Id: " + userId, null));
    }
    @PutMapping("/gmail/{gmail}/set-staff")
    @Operation(summary = "Set user as staff", description = "Set a user as staff by their email.")
    public ResponseEntity<BaseResponse> setStaff(@PathVariable String gmail) {
        UserEntity userEntity = userService.getUserByEmail(gmail);
        if (userEntity == null) {
            return ResponseEntity.badRequest().body(new SuccessResponse<>("ERROR", "User not found with email: " + gmail, null));
        }
        RoleEntity roleEntity = roleService.getByName("STAFF");
        if (roleEntity == null) {
            roleEntity = roleService.save("STAFF");
        }
        Map<Long, Boolean> roleUpdateMap = Map.of(roleEntity.getId(), true);
         userEntity = userService.updateRolesUser(roleUpdateMap, userEntity.getId());
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Set User : " + gmail + " as Staff success", userService.toUserResponseDto(userEntity)));
    }
}
