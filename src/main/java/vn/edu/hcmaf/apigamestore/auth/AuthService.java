package vn.edu.hcmaf.apigamestore.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.edu.hcmaf.apigamestore.auth.dto.LoginRequestDto;
import vn.edu.hcmaf.apigamestore.auth.dto.LoginResponseDto;
import vn.edu.hcmaf.apigamestore.auth.dto.request.RegisterRequestDto;
import vn.edu.hcmaf.apigamestore.common.util.JwtUtil;
import vn.edu.hcmaf.apigamestore.email.EmailService;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;
import vn.edu.hcmaf.apigamestore.role.RoleRepository;
import vn.edu.hcmaf.apigamestore.role.UserRole.UserRoleEntity;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserRepository;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil = new JwtUtil();
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new user with the provided registration details.
     * If the role "USER" does not exist, it will be created.
     *
     * @param requestDto The registration request containing user details (RegisterRequestDto).
     * @param role       The role to be assigned to the user (RoleEntity).
     * @return A response containing access and refresh tokens (LoginResponseDto).
     */
    public LoginResponseDto register(RegisterRequestDto requestDto, RoleEntity role) {
        // Check if the user already exists
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(requestDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUser(userEntity);
        userRoleEntity.setRole(role);

        userEntity.setUserRoles(Collections.singletonList(userRoleEntity));

        String refreshToken = jwtUtil.generateRefreshToken(userEntity.getEmail());
        userEntity.setRefreshToken(refreshToken);
        userRepository.save(userEntity);
        String token = jwtUtil.generateToken(userEntity.getEmail());

        return LoginResponseDto.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Authenticate user with email and password, generate access and refresh tokens.
     *
     * @param requestDto The login request containing email and password (LoginRequestDto).
     * @param userEntity The user entity to authenticate.
     * @return A response containing access and refresh tokens (LoginResponseDto).
     */
    public LoginResponseDto login(LoginRequestDto requestDto, UserEntity userEntity) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(userEntity.getEmail());
        String token = jwtUtil.generateToken(userEntity.getEmail());
        // Update refresh token in the database
        userEntity.setRefreshToken(refreshToken);
        userRepository.save(userEntity);
        return LoginResponseDto.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Refresh the access token using the provided refresh token.
     *
     * @param userEntity   The user entity for which to refresh the token.
     * @param refreshToken The refresh token to validate and use for generating a new access token.
     * @return A response containing the new access and refresh tokens (LoginResponseDto).
     */
    public LoginResponseDto refreshToken(UserEntity userEntity, String refreshToken) {
        String newAccessToken = jwtUtil.generateToken(userEntity.getEmail());
        return LoginResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Logout the user by clearing the refresh token.
     *
     * @param userEntity The user entity to logout.
     * @return true if logout is successful, false otherwise.
     */
    public boolean logout(UserEntity userEntity) {
        if (userEntity == null) {
            throw new NullPointerException("User cannot be null");
        }
        userEntity.setRefreshToken(null);
        userRepository.save(userEntity);
        return true;
    }
}
