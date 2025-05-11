package vn.edu.hcmaf.apigamestore.product.dto;

import lombok.Data;

@Data
public class AccountFilterRequestDto {
    private String keyword;
    private String categoryId;
    private String gameId;
    private String lowPrice;
    private String highPrice;
    private String sortBy;
}
