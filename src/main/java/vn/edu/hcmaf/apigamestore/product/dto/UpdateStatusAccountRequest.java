package vn.edu.hcmaf.apigamestore.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusAccountRequest {
  @NotBlank(message = "Trạng thái bị sai")
  private String status;
}
