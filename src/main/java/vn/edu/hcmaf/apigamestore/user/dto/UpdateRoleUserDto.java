package vn.edu.hcmaf.apigamestore.user.dto;

import lombok.Data;

import java.util.Map;
@Data
public class UpdateRoleUserDto {
    Map<Long, Boolean> roleUpdateMap;
}
