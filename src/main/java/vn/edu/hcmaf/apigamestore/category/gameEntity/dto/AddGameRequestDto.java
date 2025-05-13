package vn.edu.hcmaf.apigamestore.category.gameEntity.dto;

import lombok.Data;

@Data
public class AddGameRequestDto {
    String name;
    long categoryId;
}
