package vn.edu.hcmaf.apigamestore.cart;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.cart.dto.CartResponseDto;
import vn.edu.hcmaf.apigamestore.order.OrderDetailEntity;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.product.AccountService;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDto;
import vn.edu.hcmaf.apigamestore.redis.RedisService;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final AccountService accountService;
    private final RedisService redisService;


    /* * Converts a list of CartEntity objects to a CartResponseDto.
     * This method maps each CartEntity to an AccountDto and calculates the total price and total items.
     *
     * @param cartEntity The list of CartEntity objects to convert.
     * @return A CartResponseDto containing the accounts, total price, and total items.
     */
    public CartResponseDto toCartResponseDto(List<CartEntity> cartEntity) {
        List<AccountDto> accounts = cartEntity.stream()
                .map(cartEntity1 -> accountService.toDto(cartEntity1.getAccount()))
                .toList();
        return CartResponseDto.builder()
                .accounts(accounts)
                .totalPrice(cartEntity.stream().mapToDouble(
                        cartEntity1 -> (
                                cartEntity1.getAccount().getSalePrice() > 0) ?
                                Math.min(cartEntity1.getAccount().getSalePrice(), cartEntity1.getAccount().getPrice()) :
                                cartEntity1.getAccount().getPrice()

                ).sum())
                .totalItems(cartEntity.size())
                .build();
    }

    /**
     * Adds an account to the user's cart.
     * This method checks if the user is authenticated and if the account exists.
     * If the account is already in the cart, an exception is thrown.
     *
     * @param accountId The ID of the account to add to the cart.
     * @return A list of CartEntity objects representing the updated cart.
     * @throws IllegalArgumentException if the user is not authenticated or the account is not found.
     */
    public boolean addToCart(Long accountId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.getUserByEmail(userName);

        if (user == null) {
            throw new IllegalArgumentException("User not authenticated");
        }
        AccountEntity account = accountService.findByIdAndIsDeletedFalseAndStatusEquals(accountId, "available");
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        //check redis
        if(redisService.isProductLocked(account.getId())) {
            throw new IllegalArgumentException("Product " + account.getTitle() + " is not available, someone is buying it, please try again later");
        }
        boolean exists = cartRepository.existsByUserIdAndAccountId(user.getId(), account.getId());
        if (exists) {
            throw new IllegalArgumentException("Account already in cart");
        }

        CartEntity cartEntity = CartEntity.builder()
                .user(user)
                .account(account)
                .build();
        cartRepository.save(cartEntity);

        return true;
    }
    /**
     * Retrieves the current user's cart.
     * This method checks if the user is authenticated and returns a list of CartEntity objects.
     *
     * @return A list of CartEntity objects representing the current user's cart.
     * @throws IllegalArgumentException if the user is not authenticated.
     */
    public List<CartEntity> getCurrentUserCart(String email) {
        UserEntity user = userService.getUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not authenticated");
        }
        return cartRepository.findByUserId(user.getId());
    }

    /**
     * Removes an account from the user's cart.
     * This method checks if the user is authenticated and if the account exists in the cart.
     * If the account is not found in the cart, an exception is thrown.
     *
     * @param accountId The ID of the account to remove from the cart.
     * @return true if the account was successfully removed from the cart.
     * @throws IllegalArgumentException if the user is not authenticated, the account is not found, or the account is not in the cart.
     */
    public boolean removeFromCart(Long accountId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.getUserByEmail(userName);
        AccountEntity account = accountService.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        CartEntity cartEntity = cartRepository.findByUserIdAndAccountId(user.getId(), account.getId());
        if (cartEntity == null) {
            throw new IllegalArgumentException("Account not in cart");
        }
        cartRepository.delete(cartEntity);
        return true;
    }
    /**
        * Deletes all items in the cart for the specified user ID.
        * This method is typically used to clear the cart when the user decides to empty it.
        *
        * @param userId The ID of the user whose cart items will be deleted.
        */
    public void deleteAllItemInCart(long userId) {
        cartRepository.deleteAllByUserId(userId);
    }
}
