package vn.edu.hcmaf.apigamestore.cart;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.cart.dto.CartResponseDto;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.product.AccountService;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDto;
import vn.edu.hcmaf.apigamestore.user.UserEntity;
import vn.edu.hcmaf.apigamestore.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final AccountService accountService;

    public CartResponseDto toCartResponseDto(List<CartEntity> cartEntity) {
        List<AccountDto> accounts = cartEntity.stream()
                .map(cartEntity1 -> accountService.toDto(cartEntity1.getAccount()))
                .toList();
        return CartResponseDto.builder()
                .accounts(accounts)
                .totalPrice(cartEntity.stream().mapToDouble(cartEntity1 -> cartEntity1.getAccount().getPrice()).sum())
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
    public List<CartEntity> getCurrentUserCart() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.getUserByEmail(userName);
        if (user == null) {
            throw new IllegalArgumentException("User not authenticated");
        }
        return cartRepository.findByUserId(user.getId());
    }

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

    public boolean deleteAllItemInCart(Long accountId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.getUserByEmail(userName);
        AccountEntity account = accountService.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        List<CartEntity> cartEntities = cartRepository.findByUserId(user.getId());
        if (cartEntities.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        cartRepository.deleteAll(cartEntities);
        return true;
    }
}
