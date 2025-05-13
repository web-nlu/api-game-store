package vn.edu.hcmaf.apigamestore.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.cart.dto.CartResponseDto;
import vn.edu.hcmaf.apigamestore.common.dto.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.dto.SuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/me")
    public ResponseEntity<BaseResponse> getCurrentUserCart() {
        List<CartEntity> cartEntities = cartService.getCurrentUserCart();
        CartResponseDto cartResponseDto = cartService.toCartResponseDto(cartEntities);
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get current user cart success",cartResponseDto ));
    }
    @PutMapping("/add/{accountId}")
    public ResponseEntity<BaseResponse> addToCart(@PathVariable Long accountId) {
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Add to cart success", cartService.addToCart(accountId)));
    }
    @DeleteMapping("/remove/{accountId}")
    public ResponseEntity<BaseResponse> removeFromCart(@PathVariable Long accountId) {
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Remove from cart success", cartService.removeFromCart(accountId)));
    }
    @DeleteMapping("/remove-all/{accountId}")
    public ResponseEntity<BaseResponse> deleteAllItemInCart(@PathVariable Long accountId) {
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Delete cart success", cartService.deleteAllItemInCart(accountId)));
    }
}
