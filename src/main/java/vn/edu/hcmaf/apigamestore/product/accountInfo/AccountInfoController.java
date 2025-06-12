package vn.edu.hcmaf.apigamestore.product.accountInfo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.product.AccountService;


@RestController
@RequestMapping("/api/admin/accounts/info")
@Validated
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
/* * This controller handles requests related to account information.
 * It provides an endpoint to retrieve account information by account ID.
 */
public class AccountInfoController {
    private final AccountInfoService accountInfoService;
  private final AccountService accountService;

  /**
     * Retrieves account information by account ID.
     *
     * @param accountId The ID of the account to retrieve information for.
     * @return AccountInfoEntity containing the account information.
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountInfoByAccountId(@PathVariable long accountId) {
        // check user has buy this account in order to get account info
      AccountInfoEntity accountEntity = this.accountInfoService.getByAccountId(accountId);


      return ResponseEntity.ok().body(new SuccessResponse<>(
              "SUCCESS",
              "Lấy dữ liệu thành công",
              accountEntity != null ? accountInfoService.toDto(accountEntity): null)
      );
    }

  @PostMapping("/{accountId}")
  public ResponseEntity<?> setAccount(@PathVariable long accountId, @Valid @RequestBody AccountInfoDto accountInfoDto) {
    AccountEntity accountEntity = accountService.findById(accountId);
    AccountInfoEntity accountInfoEntity = accountInfoService.getByAccountId(accountId);
    if(accountInfoEntity != null) {
      if(accountInfoEntity.getAccount().getId() != accountEntity.getId()) {
        throw new IllegalArgumentException("Tài khoản không tồn tại");
      }
      accountInfoEntity = this.accountInfoService.updateAccount(accountInfoEntity.getId(), accountInfoDto);
    } else {
      accountInfoEntity = accountInfoService.createAccount(accountEntity, accountInfoDto);
    }
    return ResponseEntity.ok().body(new SuccessResponse<>("SUCESS", "Cập nhật thành công", accountInfoService.toDto(accountInfoEntity)));
  }
}
