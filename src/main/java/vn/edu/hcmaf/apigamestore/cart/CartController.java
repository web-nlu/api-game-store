package vn.edu.hcmaf.apigamestore.cart;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.cart.dto.CartResponseDto;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
/**
 * CartController handles operations related to the shopping cart.
 * It provides endpoints for retrieving the current user's cart,
 * adding items to the cart, removing items from the cart, and deleting all items in the cart.
 */
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    /**
     * Retrieves the current user's cart, defined by the authenticated user.
     *
     * @return A response entity containing the current user's cart details.
     */
    @Operation(summary = "Get current user cart", description = "Retrieve the current user's cart")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse> getCurrentUserCart() {
        List<CartEntity> cartEntities = cartService.getCurrentUserCart();
        CartResponseDto cartResponseDto = cartService.toCartResponseDto(cartEntities);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get current user cart success", cartResponseDto));
    }

    /**
     * Adds an item to the cart for the specified account ID.
     *
     * @param accountId The ID of the account to which the item will be added.
     * @return A response entity indicating the success of the operation.
     */
    @Operation(summary = "Add to cart", description = "Add an item to the cart for the specified account ID")
    @PutMapping("/add/{accountId}")
    public ResponseEntity<BaseResponse> addToCart(@PathVariable Long accountId) {
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Add to cart success", cartService.addToCart(accountId)));
    }

    /**
     * Removes an item from the cart for the specified account ID.
     *
     * @param accountId The ID of the account from which the item will be removed.
     * @return A response entity indicating the success of the operation.
     */
    @Operation(summary = "Remove from cart", description = "Remove an item from the cart for the specified account ID")
    @DeleteMapping("/remove/{accountId}")
    public ResponseEntity<BaseResponse> removeFromCart(@PathVariable Long accountId) {
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Remove from cart success", cartService.removeFromCart(accountId)));
    }

    /**
     * Deletes all items in the cart for the specified account ID.
     *
     * @return A response entity indicating the success of the operation.
     */
    @Operation(summary = "Delete all items in cart", description = "Delete all items in the cart for the specified account ID")
    @DeleteMapping("/remove-all")
    public ResponseEntity<BaseResponse> deleteAllItemInCart() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.getUserByEmail(userName);
        cartService.deleteAllItemInCart(user.getId());
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Delete cart success", null));
    }
}
