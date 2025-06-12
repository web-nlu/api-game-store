package vn.edu.hcmaf.apigamestore.sale_report.dto;

import java.math.BigDecimal;

import java.time.LocalDateTime;


public interface RevenueProjection {
    String getLabel();
    LocalDateTime getTime();
    Double getEpoch();
    BigDecimal getTotalRevenue();
}