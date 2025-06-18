package vn.edu.hcmaf.apigamestore.home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.product.AccountService;
import vn.edu.hcmaf.apigamestore.product.dto.AccountFilterRequestDto;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@Validated
public class PublicController {
  private final AccountService accountService;

//  @GetMapping()
//  public ResponseEntity<BaseResponse> home() {
//    accountService.filterAccountsLazyLoading(new AccountFilterRequestDto());
//  }
}
