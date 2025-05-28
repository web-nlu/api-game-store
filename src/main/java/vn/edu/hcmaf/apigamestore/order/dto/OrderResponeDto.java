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
    private Long id;
    private int orderCode;
    private Double totalPrice;
    private String status;
    private String paymentMethod;
    private String paymentLinkId;
    private long createdAt; //epoch time in milliseconds
    private long updatedAt; //epoch time in milliseconds
    List<OrderDetailResponseDto> orderDetails; // List of order details
    // Additional fields can be added as needed
}
