package vn.edu.hcmaf.apigamestore.home;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.product.AccountService;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDto;
import vn.edu.hcmaf.apigamestore.product.dto.UserHomeDataDto;
import vn.edu.hcmaf.apigamestore.product.dto.WrapDataUserHome;

import java.util.List;

@RestController
@RequestMapping("/api/home/u")
@RequiredArgsConstructor
public class UserHomeController {
    private final AccountService accountService;

    /**
     * Retrieves home data for the current user, including new accounts and top accounts across all games.
     *
     * @return ResponseEntity containing UserHomeDataDto with new accounts and top accounts.
     */
    @GetMapping()
    @Operation(summary = "Get home data", description = "Retrieve the current user's home data.")
    public ResponseEntity<BaseResponse> getHomeData() {
        List<AccountDto> newAccounts = accountService.getTop5Accounts();
        List<WrapDataUserHome> topAccountAllGames = accountService.getTopAccountAllGames();
        UserHomeDataDto userHomeDataDto = UserHomeDataDto.builder()
                .newAccounts(newAccounts)
                .topAccountAllGames(topAccountAllGames)
                .build();
        return ResponseEntity.ok().body(new SuccessResponse<>("SUCCESS", "Get home data success", userHomeDataDto));
    }
}
