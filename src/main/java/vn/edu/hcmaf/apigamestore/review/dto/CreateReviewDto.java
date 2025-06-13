package vn.edu.hcmaf.apigamestore.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReviewDto {
    @NotNull
    private String comment;
    @NotNull
    private Integer rating;
    @NotNull
    private Long accountId;
}
