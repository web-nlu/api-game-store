package vn.edu.hcmaf.apigamestore.product.accountInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts/info")
@Validated
public class AccountInfoController {
    @Autowired
    private  AccountInfoService accountInfoService;

    @GetMapping("/{accountId}")
    public AccountInfoEntity getAccountInfoByAccountId(@PathVariable long accountId) {
        // check user has buy this account in order to get account info
        return accountInfoService.findByAccountEntityId(accountId);
    }
}
