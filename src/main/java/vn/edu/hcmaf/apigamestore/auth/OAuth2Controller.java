package vn.edu.hcmaf.apigamestore.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.auth.dto.LoginResponseDto;
import vn.edu.hcmaf.apigamestore.auth.dto.request.OAuth2RequestDto;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.user.UserEntity;

@RestController
@RequestMapping("/api/oauth2/u")
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuthService oauthService;

    @GetMapping("/")
    public String home() {
        // lay code về từ Google de dung postman test, phai dung trinh duyet de goi
        // http://localhost:8080/api/oauth2/u/
        String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=744338805010-atdlkm7kd6fnmgk72i1dfcjr4s5b254k.apps.googleusercontent.com&redirect_uri=http://localhost:8080/callback&response_type=code&scope=email%20profile&state=random_state_string&access_type=offline";
        return "<a href='"+googleAuthUrl+"'>Đăng nhập bằng Google</a>";
    }

    @PostMapping("/google")
    public ResponseEntity<BaseResponse> googleLogin(@RequestBody OAuth2RequestDto request) {
        LoginResponseDto responseDto = oauthService.loginWithGoogle(request.getCode(), request.getRedirectUri());
        return ResponseEntity.ok(new SuccessResponse<>("SUCCESS", "Login successful from google code", responseDto));
    }
}
