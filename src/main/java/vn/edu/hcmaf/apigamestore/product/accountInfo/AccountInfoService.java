package vn.edu.hcmaf.apigamestore.product.accountInfo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;

@Service
@RequiredArgsConstructor
public class AccountInfoService {
    private final AccountInfoRepository accountInfoRepository;

    /**
     * Finds account information by the associated account ID.
     *
     * @param accountId The ID of the account to retrieve information for.
     * @return AccountInfoEntity containing the account information.
     * @throws IllegalArgumentException if no account info is found for the given account ID.
     */
    public AccountInfoEntity findByAccountEntityId(long accountId) {
        AccountInfoEntity accountInfoEntity = accountInfoRepository.findByAccount_Id(accountId);
        if (accountInfoEntity == null) {
            throw new IllegalArgumentException("Account info not found");
        }
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
     * Finds listed account information by the order entity.
     *
     * @param orderEntity The order entity to retrieve account information for.
     * @return AccountInfoEntity containing the account information.
     */
}
