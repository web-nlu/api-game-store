package vn.edu.hcmaf.apigamestore.order;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmaf.apigamestore.cart.CartEntity;
import vn.edu.hcmaf.apigamestore.cart.CartService;
import vn.edu.hcmaf.apigamestore.email.EmailService;
import vn.edu.hcmaf.apigamestore.order.dto.*;
import vn.edu.hcmaf.apigamestore.order.repo.OrderDetailRepository;
import vn.edu.hcmaf.apigamestore.order.repo.OrderRepository;
import vn.edu.hcmaf.apigamestore.product.AccountService;
import vn.edu.hcmaf.apigamestore.product.accountInfo.AccountInfoDto;
import vn.edu.hcmaf.apigamestore.product.accountInfo.AccountInfoEntity;
import vn.edu.hcmaf.apigamestore.product.accountInfo.AccountInfoService;
import vn.edu.hcmaf.apigamestore.redis.RedisService;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartService cartService;
    private final UserService userService;
    private final RedisService redisService;
    private final AccountService accountService;
    private final AccountInfoService accountInfoService;
    private final EmailService emailService;

    public List<OrderUserDTO> getUserOrders(OrderFilterRequestDto request, Long userId) {
      int offset = (request.getPage() - 1) * request.getSize();
      return orderRepository.filterOrders(request.getCreatedAt(), userId, request.getSearch(), request.getStatus(), request.getSize(), offset);
    }

    // Method to create a OrderResponeDto
    public OrderResponeDto toDto(OrderEntity orderEntity) {
        if (orderEntity == null) {
            return null;
        }
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findAllByOrderId(orderEntity.getId());
        List<OrderDetailResponseDto> orderDetailDtos = orderDetails.stream()
                .map(this::toDetailDto)
                .toList();
        return OrderResponeDto.builder()
                .id(orderEntity.getId())
                .orderCode(orderEntity.getOrderCode())
                .status(orderEntity.getStatus())
                .paymentMethod(orderEntity.getPaymentMethod())
                .paymentLinkId(orderEntity.getPaymentLinkId())
                .createdAt(orderEntity.getCreatedAt().getTime() / 1000) // Convert to epoch time in milliseconds
                .updatedAt(orderEntity.getUpdatedAt().getTime() / 1000)
                .orderDetails(orderDetailDtos)
                .totalPrice(orderEntity.getTotalPrice())
                .build();

    }

    public OrderDetailResponseDto toDetailDto(OrderDetailEntity orderDetailEntity) {
        if (orderDetailEntity == null) {
            return null;
        }
        return OrderDetailResponseDto.builder()
                .id(orderDetailEntity.getId())
                .orderId(orderDetailEntity.getOrder().getId())
                .productId(orderDetailEntity.getAccount().getId())
                .productName(orderDetailEntity.getAccount().getTitle())
                .price(orderDetailEntity.getAccount().getPrice())
                .build();
    }


    public OrderEntity getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

  public OrderEntity findOrderById(Long id) {
    return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
  }


  public OrderEntity createOrder() {
        // Check if the user is authenticated
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.getUserByEmail(userName);

        if (user == null) {
            throw new IllegalArgumentException("User not authenticated");
        }

        // Get the current user's cart
        List<CartEntity> cartEntities = cartService.getCurrentUserCart(user.getEmail());

        if (cartEntities.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        // check if product in cart is available
        for (CartEntity cartEntity : cartEntities) {
            if (redisService.isProductLocked(cartEntity.getAccount().getId())) {
                throw new IllegalArgumentException("Product " + cartEntity.getAccount().getTitle() + " is not available");
            }
        }
        // Lock the products in the cart
        for (CartEntity cartEntity : cartEntities) {
            redisService.lockProduct(cartEntity.getAccount().getId(), user.getId());
        }
        // Create a new order entity
        OrderEntity orderEntity = new OrderEntity();
        //Create OrderDetails
        List<OrderDetailEntity> orderDetails = cartEntities.stream()
                .map(cartEntity -> {
                    OrderDetailEntity orderDetail = new OrderDetailEntity();
                    orderDetail.setAccount(cartEntity.getAccount());
                    orderDetail.setOrder(orderEntity);
                    return orderDetail;
                }).toList();

        orderEntity.setStatus(OrderConstants.ORDER_STATUS_PENDING);
        orderEntity.setTotalPrice(cartEntities.stream()
                .mapToDouble(cartEntity -> cartEntity.getAccount().getPrice()).sum());
        orderEntity.setOrderDetails(orderDetails);
        orderEntity.setUser(user);

        return orderRepository.save(orderEntity);

    }

    public OrderEntity changeStatus(Long id, UpdateStatusOrderDTO updateStatusOrderDTO) {
      OrderEntity orderEntity = findOrderById(id);
      String paymentMethod = updateStatusOrderDTO.getPaymentMethod();
      String paymentLinkId = updateStatusOrderDTO.getPaymentLinkId();
      switch (updateStatusOrderDTO.getStatus().toUpperCase()) {
        case OrderConstants.ORDER_STATUS_COMPLETED:
          if(paymentMethod == null || paymentLinkId == null || paymentMethod.isEmpty() || paymentLinkId.isEmpty()) {
            throw new IllegalArgumentException("PaymentLinkId and PaymentLinkId cannot be empty");
          }
          orderEntity.setStatus(OrderConstants.ORDER_STATUS_COMPLETED);
          orderEntity.setPaymentMethod(paymentMethod);
          orderEntity.setPaymentLinkId(paymentLinkId);
          break;
        case OrderConstants.ORDER_STATUS_CANCELLED:
          if (orderEntity.getStatus().equals(OrderConstants.ORDER_STATUS_PENDING)) {
            orderEntity.setStatus(OrderConstants.ORDER_STATUS_CANCELLED);
          }
          break;
      }

      return orderRepository.save(orderEntity);
    }

    @Transactional
    public OrderEntity updateOrder(Long id, UpdateOrderRequestDto updateOrderRequestDto) {
        OrderEntity existingOrder = getOrderById(id);
        if (existingOrder == null) return null;
        UserEntity userEntity = existingOrder.getUser();
        // Get the current user's cart
        List<CartEntity> cartEntities = cartService.getCurrentUserCart(userEntity.getEmail());

        if (updateOrderRequestDto.getStatus().equals(OrderConstants.ORDER_STATUS_CANCELLED)) {
            existingOrder.setStatus(updateOrderRequestDto.getStatus());
            existingOrder.setPaymentMethod(updateOrderRequestDto.getPaymentMethod());
            existingOrder.setPaymentLinkId(updateOrderRequestDto.getPaymentLinkId());
        }

        if (updateOrderRequestDto.getStatus().equals(OrderConstants.ORDER_STATUS_COMPLETED)) {
            existingOrder.setStatus(updateOrderRequestDto.getStatus());
            existingOrder.setPaymentMethod(updateOrderRequestDto.getPaymentMethod());
            existingOrder.setPaymentLinkId(updateOrderRequestDto.getPaymentLinkId());

            // send mail
            List<AccountInfoEntity> accountInfoEntities = accountInfoService.findAllByOrder(existingOrder);
            if(!accountInfoEntities.isEmpty()) {
              List<AccountInfoDto> accountInfoDtos = accountInfoEntities.stream()
                      .map(accountInfoService::toDto)
                      .toList();
              emailService.sendOrderConfirmationEmail(userEntity.getEmail(), userEntity.getEmail(), String.valueOf(existingOrder.getOrderCode()), accountInfoDtos);
            }
            System.out.println("Order completed for user: " + userEntity.getEmail() + " with order code: " + updateOrderRequestDto.getOrderCode());

            // update status of products in cart
            for (CartEntity cartEntity : cartEntities) {
                accountService.updateAccountStatus(cartEntity.getAccount().getId(), OrderConstants.ACCOUNT_STATUS_SOLD);
            }
            // Clear the user's cart after completing the order
            cartService.deleteAllItemInCart(existingOrder.getUser().getId());
        }
        // Save the updated order
        return orderRepository.save(existingOrder);
    }

    public List<OrderEntity> getAllOrders() {
        // Check if the user is authenticated
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.getUserByEmail(userName);

        if (user == null) {
            throw new IllegalArgumentException("User not authenticated");
        }

        // Retrieve all orders for the authenticated user
        return orderRepository.findAllByUserId(user.getId());
    }

    public boolean checkHaveOrder(UserEntity user, Long accountId) {
      int orders = orderRepository.checkHaveOrder(user.getId(), accountId);
      return orders > 0;
    }
}
