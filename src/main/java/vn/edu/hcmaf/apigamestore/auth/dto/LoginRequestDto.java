package vn.edu.hcmaf.apigamestore.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "Username is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}
