package vn.edu.hcmaf.apigamestore.product.repo;


import org.springframework.data.domain.Page;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.product.dto.AccountFilterRequestDto;

import java.util.List;

public interface AccountRepositoryCustom {
    List<AccountEntity> filterAccounts(AccountFilterRequestDto dto);
    public Page<AccountEntity> filterAccountsLazyLoading(LazyLoadingRequestDto<AccountFilterRequestDto> request);
}