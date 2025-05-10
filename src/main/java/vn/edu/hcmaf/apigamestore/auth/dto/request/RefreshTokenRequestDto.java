package vn.edu.hcmaf.apigamestore.auth.dto.request;

import lombok.Data;

@Data
public class RefreshTokenRequestDto {
  private String refreshToken;
}
