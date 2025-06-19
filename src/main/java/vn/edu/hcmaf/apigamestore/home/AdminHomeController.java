package vn.edu.hcmaf.apigamestore.home;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;
import vn.edu.hcmaf.apigamestore.order.OrderService;
import vn.edu.hcmaf.apigamestore.order.dto.OrderAdminDataProjection;

@RestController
@RequestMapping("/api/admin/home")
@RequiredArgsConstructor
public class AdminHomeController {
    private final OrderService orderService;


    //Get Admin home Data
    @Operation(summary = "Get Admin Home Data", description = "Get admin home data including revenue projections and order statistics")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    public ResponseEntity<BaseResponse> getAdminHomeData() {
        OrderAdminDataProjection orderStatistics = orderService.getAdminOrderData();
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Get Admin Home Data success", orderStatistics));
    }
}
