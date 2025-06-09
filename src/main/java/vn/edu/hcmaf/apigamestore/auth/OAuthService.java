package vn.edu.hcmaf.apigamestore.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import vn.edu.hcmaf.apigamestore.auth.dto.LoginResponseDto;
import vn.edu.hcmaf.apigamestore.auth.dto.request.RegisterRequestDto;
import vn.edu.hcmaf.apigamestore.common.util.JwtUtil;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;
import vn.edu.hcmaf.apigamestore.role.RoleService;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserRepository;
import vn.edu.hcmaf.apigamestore.user.UserService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final RoleService roleService;
    private final String DEFAULT_PASSWORD = "1"; // Mật khẩu mặc định cho OAuth, không cần thiết

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String userInfoUri;

    public LoginResponseDto loginWithGoogle(String code, String redirectUri) {
        // 1. Đổi code lấy access_token
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        Map<String, Object> tokenResponse = restTemplate.postForObject(tokenUri, tokenRequest, Map.class);

        String accessToken = (String) tokenResponse.get("access_token");

        // 2. Lấy thông tin user từ Google
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);

        HttpEntity<?> userInfoRequest = new HttpEntity<>(userInfoHeaders);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                userInfoUri, HttpMethod.GET, userInfoRequest, Map.class
        );

        Map<String, Object> userInfo = userInfoResponse.getBody();

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        System.out.println("Email: " + email);
        System.out.println("Name: " + name);
        LoginResponseDto responseDto;
        // 3. Tùy bạn: tạo User nếu chưa có
        if (!userRepository.existsByEmailAndIsDeletedFalse(email)) {
            RegisterRequestDto registerRequestDto = new RegisterRequestDto();
            registerRequestDto.setEmail(email);
            registerRequestDto.setPassword(DEFAULT_PASSWORD);
            RoleEntity roleEntity = roleService.getByName("USER");
            if (roleEntity == null) {
                roleEntity = roleService.save("USER");
            }
            responseDto = authService.register(registerRequestDto, roleEntity);
        }else {
            // 4. Sinh JWT
            responseDto = new LoginResponseDto();
            responseDto.setAccessToken(jwtUtil.generateToken(email));
            responseDto.setRefreshToken(jwtUtil.generateRefreshToken(email));
        }

        return responseDto;
    }


}
