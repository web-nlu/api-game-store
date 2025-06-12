package vn.edu.hcmaf.apigamestore.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReviewDto {
    @NotNull
    private String comment;
    @NotNull
    private Integer stars;
    @NotNull
    private Long accountId;
    @NotNull
    private Long orderId;
    @NotNull
    private Long userId;
    private Long OldReviewId;
}
