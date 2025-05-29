package vn.edu.hcmaf.apigamestore.user.dto;

import lombok.*;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;
import vn.edu.hcmaf.apigamestore.role.dto.RoleDto;

import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String avatarUrl;
    private Boolean isActive;
    private int numOfCartItem;
    List<RoleEntity> activeRoles;
}
