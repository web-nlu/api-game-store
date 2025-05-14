package vn.edu.hcmaf.apigamestore.user.dto;

import jakarta.persistence.*;
import lombok.Data;
import vn.edu.hcmaf.apigamestore.common.BaseEntity;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;

import java.util.List;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String avatar;
}
