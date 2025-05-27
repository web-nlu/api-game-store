package vn.edu.hcmaf.apigamestore.product.accountInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.order.OrderEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountInfoService {
    private final AccountInfoRepository accountInfoRepository;

    public AccountInfoDto toDto(AccountInfoEntity accountInfoEntity) {
        return AccountInfoDto.builder()
                .accountTitle(accountInfoEntity.getAccount().getTitle())
                .username(accountInfoEntity.getUsername())
                .email(accountInfoEntity.getEmail())
                .password(accountInfoEntity.getDecryptedPassword())
                .build();
    }
    /**
     * Finds account information by the associated account ID.
     *
     * @param accountId The ID of the account to retrieve information for.
     * @return AccountInfoEntity containing the account information.
     * @throws IllegalArgumentException if no account info is found for the given account ID.
     */
    public AccountInfoEntity findByAccountEntityId(long accountId) {
        AccountInfoEntity accountInfoEntity = accountInfoRepository.findByAccount_Id(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account info not found for account ID: " + accountId));
        return accountInfoEntity;
    }

    /**
     * Saves the provided account information entity.
     *
     * @param accountInfoEntity The account information entity to save.
     * @return The saved AccountInfoEntity.
     */
    public AccountInfoEntity save(AccountInfoEntity accountInfoEntity) {
        return accountInfoRepository.save(accountInfoEntity);
    }

    /**
     * Updates the status of the account information entity by its ID.
     *
     * @param id     The ID of the account information entity to update.
     * @param status The new status to set for the account information entity.
     * @throws IllegalArgumentException if no account info is found for the given ID.
     */
    public void updateAccountInfoStatus(Long id, String status) {
        AccountInfoEntity accountInfoEntity = accountInfoRepository.findByAccount_Id(id)
                .orElseThrow(() -> new IllegalArgumentException("Account info not found"));
        accountInfoEntity.setStatus(status);
        accountInfoRepository.save(accountInfoEntity);
    }

    /**
     * Finds all account information entities associated with a specific order ID.
     *
     * @return A list of AccountInfoEntity objects associated with the order.
     * @throws IllegalArgumentException if no account info is found for the given order ID.
     */
    public List<AccountInfoEntity> findAllByOrder(OrderEntity orderEntity) {
        List<Long> accountId = orderEntity.getOrderDetails()
                .stream()
                .map(orderDetail -> orderDetail.getAccount().getId())
                .toList();
        List<AccountInfoEntity> accountInfoEntities = accountInfoRepository.findAllByAccount_IdIn(accountId);
        if (accountInfoEntities.isEmpty()) {
            throw new IllegalArgumentException("Account info not found for order ID: " + orderEntity.getId());
        }
        return accountInfoEntities;
    }

}
