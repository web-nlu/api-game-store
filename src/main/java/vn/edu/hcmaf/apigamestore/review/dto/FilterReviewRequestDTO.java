package vn.edu.hcmaf.apigamestore.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;

@Data
public class FilterReviewRequestDTO extends LazyLoadingRequestDto {
  private Integer rating;
  @NotNull
  private Boolean isHide;
}
