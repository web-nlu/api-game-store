package vn.edu.hcmaf.apigamestore.category.gameEntity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SetGameRequestDto {
    Long id;
    String name;
}
