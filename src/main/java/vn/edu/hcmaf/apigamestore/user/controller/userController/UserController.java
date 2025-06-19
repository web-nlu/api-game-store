package vn.edu.hcmaf.apigamestore.user.controller.userController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.auth.AuthService;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.ErrorResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.product.AccountService;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;
import vn.edu.hcmaf.apigamestore.user.dto.ChangePasswordDto;
import vn.edu.hcmaf.apigamestore.user.dto.UpdateUserDto;

@Slf4j
@RestController
@RequestMapping("/api/user")
@Validated
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
/**
 * UserController handles requests related to user operations.
 * It provides endpoints to retrieve the current user's information,
 * update user details, and delete a user.
 */
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    private final AccountService accountService;

    /**
     * Retrieves the current user's information.
     *
     * @return ResponseEntity containing the UserEntity wrapped in a SuccessResponse.
     */
    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get current user", description = "Retrieve the current user's information.")
    public ResponseEntity<BaseResponse> getCurrentUser() {
        UserEntity userEntity = userService.getCurrentUser();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get User info success", userService.toUserResponseDto(userEntity)));
    }



    /**
     * Updates the current user's information.
     *
     * @param updateUserDto UpdateUserDto containing the details to update.
     * @return ResponseEntity containing the updated UserEntity wrapped in a SuccessResponse.
     */
    @PutMapping("/{userId}")
    @Operation(summary = "Update user", description = "Update the current user's information.")
    public ResponseEntity<BaseResponse> updateUser(@RequestBody @Valid UpdateUserDto updateUserDto, @PathVariable Long userId) {
        UserEntity userEntity = userService.updateUser(updateUserDto, userId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Update User Id: " + userId + " success", userService.toUserResponseDto(userEntity)));
    }
    /**
     * Changes the password of the current user.
     * This endpoint allows the user to change their password.
     * It checks if the user ID matches the current user's ID or if the user has an ADMIN role.
     *
     * @param updateUserDto UpdateUserDto containing the new password.
     * @param userId The ID of the user whose password is to be changed.
     * @return ResponseEntity containing a success message wrapped in a SuccessResponse.
     */
    @PutMapping("/{userId}/change-password")
    @Operation(summary = "Change user password", description = "Change the password of the current user.")
    public ResponseEntity<BaseResponse> changePassword(@RequestBody @Valid ChangePasswordDto updateUserDto, @PathVariable Long userId) {
        // check if userId is current user or admin
        UserEntity userEntity = userService.getCurrentUser();
        if (!userEntity.getId().equals(userId) ) {
            return ResponseEntity.badRequest().body(new ErrorResponse("ERROR", "You can only change your own password", null));
        }
        userService.updatePassUser(updateUserDto.getPassword(), userId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Change password User Id: " + userId + " success",null));
    }

    /**
     * Deletes a user by user ID.
     *
     * @param userId The ID of the user to delete.
     * @return ResponseEntity containing a success message wrapped in a SuccessResponse.
     */
    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user", description = "Delete a user by user ID.")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable Long userId) {
        System.out.println("deleteUser = " + userId);
        boolean result = userService.deleteUser(userId);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Delete User Id: " + userId + " success", result));
    }
}
