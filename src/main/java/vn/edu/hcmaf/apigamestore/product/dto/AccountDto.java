package vn.edu.hcmaf.apigamestore.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private Long id;
    private String title;
    private Double price;
    private String category;
    private Long categoryId;
    private Double salePrice;
    private Long gameId;
    private String image;
    private String info;
    private String game;
}
