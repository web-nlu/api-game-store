package vn.edu.hcmaf.apigamestore.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponeDto {
    private Long orderId;
    private int orderCode;
    private Double totalPrice;
    private String status;
    private String paymentMethod;
    private String paymentLinkId;
    private String createdAt; //epoch time in milliseconds
    private String updatedAt; //epoch time in milliseconds
    List<OrderDetailResponeDto> orderDetails; // List of order details
    // Additional fields can be added as needed
}
