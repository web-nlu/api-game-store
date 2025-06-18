package vn.edu.hcmaf.apigamestore.order.controller.adminController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.order.OrderConstants;
import vn.edu.hcmaf.apigamestore.order.OrderEntity;
import vn.edu.hcmaf.apigamestore.order.OrderService;
import vn.edu.hcmaf.apigamestore.order.dto.*;
import vn.edu.hcmaf.apigamestore.sale_report.dto.RevenueProjection;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin/orders/")
@RequiredArgsConstructor
public class OrderAdminController {
    private final OrderService orderService;

    @Operation(summary = "Get Order Detail", description = "Get order detail by order ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getOrderDetail(@PathVariable Long id) {
        OrderEntity orderEntity = orderService.findOrderById(id);
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Get Order Id : " + id + " success", orderService.toDto(orderEntity)));
    }

    @Operation(summary = "Get All Orders", description = "Get all orders for the user")
    @GetMapping("/filter")
    public ResponseEntity<BaseResponse> getAllOrders(@ModelAttribute @Valid OrderFilterRequestDto request) {
        List<OrderUserDTO> orders = orderService.getUserOrders(request, null);
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Get all orders success", orders));
    }

    @Operation(summary = "Update Order", description = "Update an existing order by ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/status/{id}")
    public ResponseEntity<BaseResponse> cancel(@PathVariable Long id, @RequestBody @Valid UpdateStatusOrderDTO request) {
        OrderEntity updatedOrder = orderService.changeStatus(id, request);
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Order updated successfully", orderService.toDto(updatedOrder)));
    }

}
