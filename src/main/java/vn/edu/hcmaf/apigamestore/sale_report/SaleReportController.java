package vn.edu.hcmaf.apigamestore.sale_report;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmaf.apigamestore.common.response.BaseResponse;
import vn.edu.hcmaf.apigamestore.common.response.SuccessResponse;

import vn.edu.hcmaf.apigamestore.sale_report.dto.RevenueProjection;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/a/u/sale-reports")
@RequiredArgsConstructor
@Tag(name = "Sale Report", description = "Sale Report API for Admin")
public class SaleReportController {
    private final SaleReportService saleReportService;
    private final ExportExcelService exportExcelService;
    //GET http://localhost:8080/api/admin/sale-reports/revenue?type=DAY&startEpoch=1704067200&endEpoch=1746115199
    //GET http://localhost:8080/api/admin/sale-reports/revenue?type=WEEK&startEpoch=1704067200&endEpoch=1746115199
    //GET http://localhost:8080/api/admin/sale-reports/revenue?type=MONTH&startEpoch=1704067200&endEpoch=1746115199
    @Operation(summary = "Get Revenue by Epoch", description = "Get revenue report by epoch time")
    @GetMapping("/revenue")
    public ResponseEntity<BaseResponse> getRevenue(
            @RequestParam String type,
            @RequestParam Long startEpoch,
            @RequestParam Long endEpoch
    ) {
        LocalDateTime startDate = Instant.ofEpochSecond(startEpoch)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime endDate = Instant.ofEpochSecond(endEpoch)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return ResponseEntity.ok().body(
                new SuccessResponse<>("SUCCESS", "Lấy báo cáo doanh thu thành công", saleReportService.getRevenue(startDate, endDate, type))
        );
    }
    @Operation(summary = "Export Revenue to Excel", description = "Export revenue report to Excel file")
    @GetMapping("/revenue/export")
    public ResponseEntity<Void> exportRevenueToExcel(
            @RequestParam String type,
            @RequestParam Long startEpoch,
            @RequestParam Long endEpoch,
            HttpServletResponse response
    ) {

        LocalDateTime startDate = Instant.ofEpochSecond(startEpoch)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime endDate = Instant.ofEpochSecond(endEpoch)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        List<RevenueProjection> revenues = saleReportService.getRevenue(startDate, endDate, type);
        ExportExcelService exportExcelService = new ExportExcelService();
        try {
            exportExcelService.exportRevenueToExcel(revenues, response);
        } catch (IOException e) {
            throw new RuntimeException("Error exporting revenue to Excel", e);
        }
        return ResponseEntity.ok().build();
    }
}
