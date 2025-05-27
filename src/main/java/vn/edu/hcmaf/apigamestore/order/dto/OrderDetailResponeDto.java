package vn.edu.hcmaf.apigamestore.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponeDto {
    private Long orderDetailId;
    private Long orderId; // ID of the associated order
    private Long productId;
    private String productName;
    private Double price; // Price per item
    private Double totalPrice; // Total price for this order detail
}
