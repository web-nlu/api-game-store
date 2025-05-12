package vn.edu.hcmaf.apigamestore.auth;

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
import vn.edu.hcmaf.apigamestore.role.RoleEntity;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserRepository;

import java.util.Collections;

@Slf4j
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil = new JwtUtil();
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDto register(RegisterRequestDto requestDto, RoleEntity role) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("Username already exists");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(requestDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userEntity.setRoles(Collections.singletonList(role));
        String refreshToken = jwtUtil.generateRefreshToken(userEntity.getEmail());
        userEntity.setRefreshToken(refreshToken);
        userRepository.save(userEntity);
        String token = jwtUtil.generateToken(userEntity.getEmail());

        return LoginResponseDto.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

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

    public LoginResponseDto refreshToken(UserEntity userEntity, String refreshToken) {
        String newAccessToken = jwtUtil.generateToken(userEntity.getEmail());
        return LoginResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean logout(UserEntity userEntity) {
        if (userEntity == null) {
            throw new NullPointerException("User cannot be null");
        }
        userEntity.setRefreshToken(null);
        userRepository.save(userEntity);
        return true;
    }
}
