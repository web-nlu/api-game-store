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

    public AccountInfoDto toDto(AccountInfoEntity accountInfoEntity) {
        return AccountInfoDto.builder()
                .username(accountInfoEntity.getUsername())
                .email(accountInfoEntity.getEmail())
                .password(accountInfoEntity.getDecryptedPassword())
                .build();
    }

    public AccountInfoEntity findByAccountEntityId(long accountId) {
        AccountInfoEntity accountInfoEntity = accountInfoRepository.findByAccount_Id(accountId);
        if (accountInfoEntity == null) {
            throw new IllegalArgumentException("Account info not found");
        }
        return accountInfoEntity;
    }
    public AccountInfoEntity save(AccountInfoEntity accountInfoEntity) {
        return accountInfoRepository.save(accountInfoEntity);
    }
}
