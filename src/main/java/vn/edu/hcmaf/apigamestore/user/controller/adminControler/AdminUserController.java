package vn.edu.hcmaf.apigamestore.user.controller.adminControler;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingResponseDto;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
@Validated
/**
 * AdminUserController handles requests related to user management for administrators.
 * It provides endpoints to retrieve user information, including lazy loading support.
 */
public class AdminUserController {
    private final UserService userService;
    /**
     * Retrieves all users in the system.
     *
     * @return ResponseEntity containing a list of UserEntity objects wrapped in a SuccessResponse.
     */
    @GetMapping("/all")
    @Operation(summary = "Get all users", description = "Retrieve a list of all users in the system.")
    public ResponseEntity<BaseResponse> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get all User info success", users));
    }
    /**
     * Retrieves all users with lazy loading support.
     *
     * @param lazyLoadingRequestDto LazyLoadingRequestDto containing pagination and sorting information.
     * @return ResponseEntity containing a LazyLoadingResponseDto with a list of UserEntity objects wrapped in a SuccessResponse.
     */
    @PostMapping("/all-lazyloading")
    @Operation(summary = "Get all users with lazy loading", description = "Retrieve a list of all users in the system with lazy loading support.")
    public ResponseEntity<BaseResponse> getAllUsersLazyLoading(@RequestBody @Valid LazyLoadingRequestDto lazyLoadingRequestDto) {
        LazyLoadingResponseDto<List<UserEntity>> users = userService.getAllUsersLazyLoading(lazyLoadingRequestDto);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get all User info success (lazyloading)", users));
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
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get User Id : "+ userId+" info success", userEntity));
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
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get User Name : "+ username+" info success", userEntity));
    }
}
