package vn.edu.hcmaf.apigamestore.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDto;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserHomeDataDto {
    List<AccountDto> newAccounts;
    List<WrapDataUserHome> topAccountAllGames;
}
