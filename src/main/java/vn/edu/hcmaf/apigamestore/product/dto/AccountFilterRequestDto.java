package vn.edu.hcmaf.apigamestore.product.dto;

import lombok.Data;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;

@Data
public class AccountFilterRequestDto extends LazyLoadingRequestDto {
    private String keyword;
    private String categoryId;
    private String gameId;
    private String lowPrice;
    private String highPrice;
    private String sortBy;
}
