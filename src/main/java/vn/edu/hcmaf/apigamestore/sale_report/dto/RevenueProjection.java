package vn.edu.hcmaf.apigamestore.sale_report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RevenueProjection {
    String label;
    BigDecimal TotalRevenue;
}