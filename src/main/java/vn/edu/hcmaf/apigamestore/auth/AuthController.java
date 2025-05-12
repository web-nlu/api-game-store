package vn.edu.hcmaf.apigamestore.auth;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.auth.dto.LoginRequestDto;
import vn.edu.hcmaf.apigamestore.auth.dto.LoginResponseDto;
import vn.edu.hcmaf.apigamestore.auth.dto.request.RefreshTokenRequestDto;
import vn.edu.hcmaf.apigamestore.auth.dto.request.RegisterRequestDto;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.ErrorResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;
import vn.edu.hcmaf.apigamestore.common.util.JwtUtil;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;
import vn.edu.hcmaf.apigamestore.role.RoleService;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final AuthService authService;
    private final RoleService roleService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(RoleService roleService, AuthService authService, UserService userService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.roleService = roleService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody @Valid RegisterRequestDto request) {
        log.info("Register attempt for user: {}", request);
        RoleEntity roleEntity = roleService.getByName("USER");
        if (roleEntity == null) {
            roleEntity = roleService.save("USER");
        }
        LoginResponseDto loginResponseDto = authService.register(request, roleEntity);

        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Reregister success", loginResponseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@RequestBody @Valid LoginRequestDto request) {
        log.info("Login attempt for user: {}", request);
        UserEntity user = userService.getUserByEmail(request.getEmail());
        LoginResponseDto dto = authService.login(request, user);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Login success", dto));
    }

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
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Get refresh-token success", loginResponseDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse> logout(@RequestHeader("Authorization") String token) {
        String finalToken = token.replace("Bearer ", "");
        jwtUtil.validateToken(finalToken);
        String email = jwtUtil.getEmailFromToken(finalToken);
        UserEntity userEntity = userService.getUserByEmail(email);
        boolean result = authService.logout(userEntity);

        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS","Logout success", result));
    }
}
