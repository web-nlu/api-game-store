package vn.edu.hcmaf.apigamestore.auth;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.auth.dto.LoginRequestDto;
import vn.edu.hcmaf.apigamestore.auth.dto.RegisterRequestDto;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody @Valid RegisterRequestDto request) {
        log.info("Register attempt for user: {}", request);
        return authService.register(request.getUsername(), request.getPassword());
    }
    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@RequestBody @Valid LoginRequestDto request) {
        log.info("Login attempt for user: {}", request);
        return authService.login(request.getUsername(), request.getPassword());
    }
//    @PostMapping("/relogin")
//    public ResponseEntity<BaseResponse> reLogin(@RequestHeader("Authorization") String token) {
//        return authService.reLogin(token);
//    }
    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponse> refreshToken(@RequestHeader("Authorization") String token) {
        return authService.refreshToken(token);
    }
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse> logout(@RequestHeader("Authorization") String token) {
        return authService.logout(token);
    }


}
