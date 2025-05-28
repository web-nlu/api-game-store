package vn.edu.hcmaf.apigamestore.order.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderUserDTO {
  private Long id;
  private Integer orderCode;
  private Double totalPrice;
  private String status;
  private String paymentMethod;
  private String paymentLinkId;
  private Long createdAt;
  private Long userId;
  private String phoneNumber;
  private String email;
}