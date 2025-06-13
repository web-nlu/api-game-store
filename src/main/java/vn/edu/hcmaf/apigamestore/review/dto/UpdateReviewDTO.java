package vn.edu.hcmaf.apigamestore.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateReviewDTO {
  @NotNull
  private String comment;
}
