package vn.edu.hcmaf.apigamestore.product.accountInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/accounts/info")
@Validated
/* * This controller handles requests related to account information.
 * It provides an endpoint to retrieve account information by account ID.
 */
public class AccountInfoController {
    @Autowired
    private  AccountInfoService accountInfoService;
    /**
     * Retrieves account information by account ID.
     *
     * @param accountId The ID of the account to retrieve information for.
     * @return AccountInfoEntity containing the account information.
     */
    @GetMapping("/{accountId}")
    public AccountInfoEntity getAccountInfoByAccountId(@PathVariable long accountId) {
        // check user has buy this account in order to get account info
        return accountInfoService.findByAccountEntityId(accountId);
    }
}
