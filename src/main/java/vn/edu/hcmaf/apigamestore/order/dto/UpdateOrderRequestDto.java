package vn.edu.hcmaf.apigamestore.order.dto;

import lombok.Data;

@Data
public class UpdateOrderRequestDto {
    private String status;
    private int orderCode;
    private String paymentMethod;
    private String paymentLinkId;

    // Additional fields can be added as needed
}
