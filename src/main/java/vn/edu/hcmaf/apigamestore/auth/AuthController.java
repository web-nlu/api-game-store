package vn.edu.hcmaf.apigamestore.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.auth.dto.LoginRequestDto;
import vn.edu.hcmaf.apigamestore.auth.dto.LoginResponseDto;
import vn.edu.hcmaf.apigamestore.auth.dto.request.RefreshTokenRequestDto;
import vn.edu.hcmaf.apigamestore.auth.dto.request.RegisterRequestDto;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.ErrorResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.common.util.JwtUtil;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;
import vn.edu.hcmaf.apigamestore.role.RoleService;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;

@Slf4j
@RestController
@RequestMapping("/api/auth/u")
@Validated
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication API")
/**
 * AuthController handles user authentication, registration, login, token validation, and logout.
 * It provides endpoints for checking token validity, registering new users, logging in,
 * refreshing tokens, and logging out.
 */
public class AuthController {
    private final AuthService authService;
    private final RoleService roleService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * Check token from header if not valid, return 401 catch by JwtAuthenticationFilter
     * if valid, return 200 with true
     * @param token
     * @return
     */
    @Operation(summary = "Check token", description = "Check token from header")
    @GetMapping("/check-token")
    public ResponseEntity<BaseResponse> checkToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Check token success", true));
    }
    /**
     * Register a new user with the provided registration details.
     * If the role "USER" does not exist, it will be created.
     *
     * @param request The registration request containing user details (RegisterRequestDto).
     * @return A response entity containing the registration result.
     */
    @Operation(summary = "Register", description = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody @Valid RegisterRequestDto request) {
        log.info("Register attempt for user: {}", request);
        RoleEntity roleEntity = roleService.getByName("USER");
        if (roleEntity == null) {
            roleEntity = roleService.save("USER");
        }
        LoginResponseDto loginResponseDto = authService.register(request, roleEntity);

        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Reregister success", loginResponseDto));
    }
    /**
     * Login a user with the provided email and password.
     * If the user does not exist, it will return an error response.
     *
     * @param request The login request containing email and password (LoginRequestDto).
     * @return A response entity containing the login result.
     */
    @Operation(summary = "Login", description = "Login with email and password")
    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@RequestBody @Valid LoginRequestDto request) {
        log.info("Login attempt for user: {}", request);
        UserEntity user = userService.getUserByEmail(request.getEmail());
        LoginResponseDto dto = authService.login(request, user);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Login success", dto));
    }
    /**
     * Refresh the token using the provided refresh token.
     * If the refresh token is invalid or does not match the user's stored refresh token,
     * it will return an error response.
     *
     * @param request The request containing the refresh token (RefreshTokenRequestDto).
     * @return A response entity containing the new login response with refreshed tokens.
     */
    @Operation(summary = "Refresh token", description = "Refresh token with refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponse> refreshToken(@RequestBody @Valid RefreshTokenRequestDto request) {
        jwtUtil.validateToken(request.getRefreshToken());
        String email = jwtUtil.getEmailFromToken(request.getRefreshToken());
        UserEntity userEntity = userService.getUserByEmail(email);
        if (userEntity == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("400", "FAIL", "User not found"));
        } else if (!request.getRefreshToken().equals(userEntity.getRefreshToken())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("400", "FAIL", "Invalid refresh token, please login again"));
        }
        LoginResponseDto loginResponseDto = authService.refreshToken(userEntity, request.getRefreshToken());
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get refresh-token success", loginResponseDto));
    }
    /**
     * Logout the user by validating the provided token and removing the user's session.
     * If the token is invalid or the user does not exist, it will return an error response.
     *
     * @param token The authorization token from the request header.
     * @return A response entity indicating the logout result.
     */
    @Operation(summary = "Logout", description = "Logout with token")
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse> logout(@RequestHeader("Authorization") String token) {
        String finalToken = token.replace("Bearer ", "");
        jwtUtil.validateToken(finalToken);
        String email = jwtUtil.getEmailFromToken(finalToken);
        UserEntity userEntity = userService.getUserByEmail(email);
        boolean result = authService.logout(userEntity);

        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Logout success", result));
    }
}
