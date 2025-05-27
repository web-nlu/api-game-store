package vn.edu.hcmaf.apigamestore.product.accountInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoDto {
    String accountTitle;
    String username;
    String email;
    String password;
}
