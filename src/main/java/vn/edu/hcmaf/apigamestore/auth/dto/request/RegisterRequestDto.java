package vn.edu.hcmaf.apigamestore.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotBlank(message = "Password is required")
    @Email
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}