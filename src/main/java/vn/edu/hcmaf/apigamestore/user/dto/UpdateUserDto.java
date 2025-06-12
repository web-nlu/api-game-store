package vn.edu.hcmaf.apigamestore.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import vn.edu.hcmaf.apigamestore.role.RoleEntity;

@Data
public class UpdateUserDto {

    @Pattern(
            regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,11}$",
            message = "Số điện thoại không hợp lệ"
    )
    private String phoneNumber;

    @Size(max = 255, message = "Địa chỉ không được dài quá 255 ký tự")
    private String address;

//    @Pattern(
//            regexp = "^(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|jpeg|png)$",
//            message = "Avatar phải là URL ảnh hợp lệ (jpg, jpeg, png)"
//    )
    private String avatar;

//    private String[] roles;
}
