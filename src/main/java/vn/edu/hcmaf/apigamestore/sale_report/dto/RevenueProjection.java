package vn.edu.hcmaf.apigamestore.sale_report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmaf.apigamestore.order.repo.OrderRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public interface RevenueProjection {
    String getLabel();
    LocalDateTime getTime();
    Double getEpoch();
    BigDecimal getTotalRevenue();
}