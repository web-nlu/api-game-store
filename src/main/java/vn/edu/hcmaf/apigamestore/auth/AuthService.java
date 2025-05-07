package vn.edu.hcmaf.apigamestore.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.edu.hcmaf.apigamestore.auth.dto.LoginResponseDto;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.ErrorResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;
import vn.edu.hcmaf.apigamestore.common.util.JwtUtil;
import vn.edu.hcmaf.apigamestore.role.RoleRepository;
import vn.edu.hcmaf.apigamestore.role.RoleService;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final JwtUtil jwtUtil = new JwtUtil();
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<BaseResponse> register(String username, String password) {
        System.out.println("username = " + username);
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password)); // mã hóa mật khẩu
        // Set role for user
        userEntity.setRoles(Collections.singletonList(roleService.findByName("USER").orElseThrow(() -> new IllegalArgumentException("Role not found"))));
        // Generate refresh token
        String refreshToken = jwtUtil.generateRefreshToken(userEntity.getUsername());
        userEntity.setRefreshToken(refreshToken);
        userRepository.save(userEntity);
        // Generate JWT token
        String accessToken = jwtUtil.generateToken(userEntity.getUsername());
        return ResponseEntity.ok().body(new SuccessResponse<LoginResponseDto>
                ("200"
                        , "User registered successfully"
                        , LoginResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .id(userEntity.getId())
                        .username(userEntity.getUsername())
                        .email(userEntity.getEmail())
                        .avatar(userEntity.getAvatar())
                        .build()
                )
        );
    }

    public ResponseEntity<BaseResponse> login(String username, String password) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        UserEntity userEntity = userOpt.get();
        // Generate JWT token
        String accessToken = jwtUtil.generateToken(username);
        // Generate refresh token
        String refreshToken = jwtUtil.generateRefreshToken(username);
        // Save refresh token to user entity
        userEntity.setRefreshToken(refreshToken);
        userRepository.save(userEntity);


        return ResponseEntity.ok().body(new SuccessResponse<LoginResponseDto>("200", "Login successful",
                LoginResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .id(userEntity.getId())
                        .username(userEntity.getUsername())
                        .email(userEntity.getEmail())
                        .avatar(userEntity.getAvatar())
                        .build()));
    }

//    public ResponseEntity<BaseResponse> reLogin(String token) {
//        String finalToken = token.replace("Bearer ", "");
//        if (!jwtUtil.validateToken(finalToken)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("401", "Invalid token"));
//        }
//        String username = jwtUtil.getUsernameFromToken(finalToken);
//        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
//        return userOpt.<ResponseEntity<BaseResponse>>map(user -> ResponseEntity.ok().body(new SuccessResponse<LoginResponse>("200", "Token is valid", LoginResponse.builder().accessToken(finalToken).refreshToken(user.getRefreshToken()).username(user.getUsername()).email(user.getEmail()).avatar(user.getAvatar()).build()))).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("401", "user not found")));
//    }

    public ResponseEntity<BaseResponse> refreshToken(String token) {
        String finalToken = token.replace("Bearer ", "");
        if (!jwtUtil.validateToken(finalToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("401","UNAUTHORIZED", "Invalid token"));
        }
        String username = jwtUtil.getUsernameFromToken(finalToken);
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        UserEntity userEntity = userOpt.get();
        String refreshToken = userEntity.getRefreshToken();
        if (!refreshToken.equals(finalToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String newAccessToken = jwtUtil.generateToken(username);
        return ResponseEntity.ok().body(new SuccessResponse<LoginResponseDto>("200", "Token refreshed successfully", LoginResponseDto.builder().accessToken(newAccessToken).build()));
    }

    public ResponseEntity<BaseResponse> logout(String token) {
        String finalToken = token.replace("Bearer ", "");
        if (!jwtUtil.validateToken(finalToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("401", "UNAUTHORIZED","Invalid token"));
        }
        String username = jwtUtil.getUsernameFromToken(finalToken);
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            UserEntity userEntity = userOpt.get();
            userEntity.setRefreshToken(null);
            userRepository.save(userEntity);
            return ResponseEntity.ok(new SuccessResponse<>("200", "Logout successful", null));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("401", "UNAUTHORIZED","User not found"));
        }

    }


}
