package vn.edu.hcmaf.apigamestore.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WrapDataUserHome {
    private Long gameId;
    private String gameName;
    private Long categoryId;
    private List<AccountDto> top5Accounts;
}
