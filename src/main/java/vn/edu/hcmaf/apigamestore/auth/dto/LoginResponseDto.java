package vn.edu.hcmaf.apigamestore.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;


import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long id;
    private String username;
    private List<RoleEntity> roles;
    private String email;
    private String avatar;
}
