package vn.edu.hcmaf.apigamestore.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDto {
  @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
  private String password;
}
