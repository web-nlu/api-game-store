package vn.edu.hcmaf.apigamestore.product.accountInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;

import java.util.Optional;

public interface AccountInfoRepository extends JpaRepository<AccountInfoEntity, Long> {


    AccountInfoEntity findByAccount_Id(long accountId);
}
