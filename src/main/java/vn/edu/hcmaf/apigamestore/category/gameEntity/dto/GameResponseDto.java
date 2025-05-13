package vn.edu.hcmaf.apigamestore.category.gameEntity.dto;

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
public class GameResponseDto {
    long id;
    String name;
    long categoryId;
    List<AccountDto> accounts;
}
