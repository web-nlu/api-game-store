package vn.edu.hcmaf.apigamestore.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotBlank(message = "Password is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}