package vn.edu.hcmaf.apigamestore.sale_report;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;

import vn.edu.hcmaf.apigamestore.order.OrderService;
import vn.edu.hcmaf.apigamestore.sale_report.dto.StatisticFilterRequest;
import vn.edu.hcmaf.apigamestore.sale_report.dto.RevenueProjection;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sale-reports")
@RequiredArgsConstructor
@Tag(name = "Sale Report", description = "Sale Report API for Admin")
public class SaleReportController {
    private final SaleReportService saleReportService;
    private final ExportExcelService exportExcelService;
    private final OrderService orderService;
    //GET http://localhost:8080/api/admin/sale-reports/revenue?type=DAY&startDate=1704067200&endDate=1746115199
    //GET http://localhost:8080/api/admin/sale-reports/revenue?type=WEEK&startDate=1704067200&endDate=1746115199
    //GET http://localhost:8080/api/admin/sale-reports/revenue?type=MONTH&startDate=1704067200&endDate=1746115199
    @Operation(summary = "statistic", description = "Get Statistic Order")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/revenue")
    public ResponseEntity<BaseResponse> getStatistic(@ModelAttribute @Valid StatisticFilterRequest request) {
      List<RevenueProjection> statistic = orderService.getRevenue(request);
      return ResponseEntity.ok().body(
              new SuccessResponse<>("SUCCESS", "Get Statistic Order success", statistic)
      );
    }
//    @Operation(summary = "Export Revenue to Excel", description = "Export revenue report to Excel file")
//    @GetMapping("/revenue/export")
//    public ResponseEntity<Void> exportRevenueToExcel(
//            @RequestParam String type,
//            @RequestParam Long startEpoch,
//            @RequestParam Long endEpoch,
//            HttpServletResponse response
//    ) {
//
//        LocalDateTime startDate = Instant.ofEpochSecond(startEpoch)
//                .atZone(ZoneId.systemDefault())
//                .toLocalDateTime();
//        LocalDateTime endDate = Instant.ofEpochSecond(endEpoch)
//                .atZone(ZoneId.systemDefault())
//                .toLocalDateTime();
//
//        List<RevenueProjection> revenues = saleReportService.getRevenue(startDate, endDate, type);
//        ExportExcelService exportExcelService = new ExportExcelService();
//        try {
//            exportExcelService.exportRevenueToExcel(revenues, response);
//        } catch (IOException e) {
//            throw new RuntimeException("Error exporting revenue to Excel", e);
//        }
//        return ResponseEntity.ok().build();
//    }
}
