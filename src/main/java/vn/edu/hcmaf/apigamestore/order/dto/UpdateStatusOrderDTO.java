package vn.edu.hcmaf.apigamestore.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusOrderDTO {
  @NotNull
  private String status;
  private String paymentMethod;
  private String paymentLinkId;
}
