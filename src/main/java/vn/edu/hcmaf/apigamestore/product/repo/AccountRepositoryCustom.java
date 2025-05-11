package vn.edu.hcmaf.apigamestore.product.repo;

import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.product.dto.AccountFilterRequestDto;

import java.util.List;

public interface AccountRepositoryCustom {
    List<AccountEntity> filterAccounts(AccountFilterRequestDto dto);
}