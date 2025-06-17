package vn.edu.hcmaf.apigamestore.user.dto;

import lombok.Data;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;

@Data
public class FilterUserRequestDTO extends LazyLoadingRequestDto {
  private String role;
  private String email;
}
