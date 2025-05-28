package vn.edu.hcmaf.apigamestore.order.controller.userController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.order.OrderConstants;
import vn.edu.hcmaf.apigamestore.order.OrderEntity;
import vn.edu.hcmaf.apigamestore.order.OrderService;
import vn.edu.hcmaf.apigamestore.order.dto.OrderFilterRequestDto;
import vn.edu.hcmaf.apigamestore.order.dto.OrderUserDTO;
import vn.edu.hcmaf.apigamestore.order.dto.UpdateOrderRequestDto;
import vn.edu.hcmaf.apigamestore.order.dto.WebhookResponse;
import vn.edu.hcmaf.apigamestore.order.enums.PaymentMethod;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders/")
@Tag(name = "Order", description = "Order API for User")
public class OrderUserController {
  private final OrderService orderService;
  private final UserService userService;

  @Operation(summary = "Get Order Detail", description = "Get order detail by order ID")
  @GetMapping("/{id}")
  public ResponseEntity<BaseResponse> getOrderDetail(@PathVariable Long id) {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    UserEntity user = userService.getUserByEmail(userName);
    OrderEntity orderEntity = orderService.findOrderById(id);
    if (!Objects.equals(user.getId(), orderEntity.getUser().getId())) {
      throw new IllegalArgumentException("Order not found with id: " + id);
    }
    return ResponseEntity.ok().body(
            new SuccessResponse<>("SUCCESS", "Get Order Id : " + id + " success", orderService.toDto(orderEntity)));
  }

  @Operation(summary = "Get All Orders", description = "Get all orders for the user")
  @GetMapping("/filter")
  public ResponseEntity<BaseResponse> getAllOrders(@ModelAttribute @Valid OrderFilterRequestDto request) {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    UserEntity user = userService.getUserByEmail(userName);
    List<OrderUserDTO> orders = orderService.getUserOrders(request, user.getId());
    return ResponseEntity.ok().body(
            new SuccessResponse<>("SUCCESS", "Get all orders success", orders));
  }

  @Operation(summary = "Create Order", description = "Create a new order")
  @PostMapping("/create")
  public ResponseEntity<BaseResponse> createOrder() {
    OrderEntity newOrder = orderService.createOrder();
    return ResponseEntity.ok().body(
            new SuccessResponse<>("SUCCESS", "Order created successfully", orderService.toDto(newOrder)));
  }

  // Todo: nguy hiem ve bao mat
  @Operation(summary = "Update Order", description = "Update an existing order by ID")
  @PutMapping("/{id}/update")
  public ResponseEntity<BaseResponse> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderRequestDto updateOrderRequestDto) {
    OrderEntity updatedOrder = orderService.updateOrder(id, updateOrderRequestDto);
    return ResponseEntity.ok().body(
            new SuccessResponse<>("SUCCESS", "Order updated successfully", orderService.toDto(updatedOrder)));
  }

  @Operation(summary = "Update Order", description = "Update an existing order by ID")
  @PutMapping("cancel/{id}")
  public ResponseEntity<BaseResponse> cancel(@PathVariable Long id) {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
    UserEntity user = userService.getUserByEmail(userName);
    OrderEntity orderEntity = orderService.findOrderById(id);
    if (!Objects.equals(user.getId(), orderEntity.getUser().getId())) {
      throw new IllegalArgumentException("Order not found with id: " + id);
    }
    UpdateOrderRequestDto updateOrderRequestDto = new UpdateOrderRequestDto();
    updateOrderRequestDto.setStatus(OrderConstants.ORDER_STATUS_CANCELLED);
    OrderEntity updatedOrder = orderService.updateOrder(id, updateOrderRequestDto);
    return ResponseEntity.ok().body(
            new SuccessResponse<>("SUCCESS", "Order updated successfully", orderService.toDto(updatedOrder)));
  }

  @PostMapping("/u/receive-hook")
  public ResponseEntity<BaseResponse> receiveHook(@RequestBody WebhookResponse webhookResponse) {
    long id = webhookResponse.getData().getOrderCode();
    UpdateOrderRequestDto updateOrderRequestDto = new UpdateOrderRequestDto();
    updateOrderRequestDto.setStatus(OrderConstants.ORDER_STATUS_CANCELLED);
    if(webhookResponse.isSuccess()) {
      updateOrderRequestDto.setStatus(OrderConstants.ORDER_STATUS_COMPLETED);
    }
    updateOrderRequestDto.setPaymentMethod(String.valueOf(PaymentMethod.BANK));
    updateOrderRequestDto.setPaymentLinkId(webhookResponse.getData().getPaymentLinkId());

    orderService.updateOrder(id, updateOrderRequestDto);
    return ResponseEntity.ok().body(
            new SuccessResponse<>("SUCCESS", "Order updated successfully", null));
  }

}
