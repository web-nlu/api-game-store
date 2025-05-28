package vn.edu.hcmaf.apigamestore.order.controller.userController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.order.OrderConstants;
import vn.edu.hcmaf.apigamestore.order.OrderEntity;
import vn.edu.hcmaf.apigamestore.order.OrderService;
import vn.edu.hcmaf.apigamestore.order.dto.UpdateOrderRequestDto;
import vn.edu.hcmaf.apigamestore.order.dto.WebhookResponse;
import vn.edu.hcmaf.apigamestore.order.enums.PaymentMethod;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders/")
@Tag(name = "Order", description = "Order API for User")
public class OrderUserController {
  private final OrderService orderService;

  @Operation(summary = "Get Order Detail", description = "Get order detail by order ID")
  @GetMapping("/{id}")
  public ResponseEntity<BaseResponse> getOrderDetail(@PathVariable Long id) {
    OrderEntity orderEntity = orderService.getOrderById(id);
    return ResponseEntity.ok().body(
            new SuccessResponse<>("SUCCESS", "Get Order Id : " + id + " success", orderService.toDto(orderEntity)));
  }

  @Operation(summary = "Get All Orders", description = "Get all orders for the user")
  @GetMapping("/all")
  public ResponseEntity<BaseResponse> getAllOrders() {
    List<OrderEntity> orderEntities = orderService.getAllOrders();
    List<?> orderDtos = orderEntities.stream()
            .map(orderService::toDto)
            .toList();
    return ResponseEntity.ok().body(
            new SuccessResponse<>("SUCCESS", "Get all orders success", orderDtos));
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

    OrderEntity updatedOrder = orderService.updateOrder(id, updateOrderRequestDto);
    return ResponseEntity.ok().body(
            new SuccessResponse<>("SUCCESS", "Order updated successfully", orderService.toDto(updatedOrder)));
  }

}
