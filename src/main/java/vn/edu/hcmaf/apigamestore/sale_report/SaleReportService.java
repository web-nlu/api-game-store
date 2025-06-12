package vn.edu.hcmaf.apigamestore.sale_report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hcmaf.apigamestore.order.repo.OrderRepository;

import vn.edu.hcmaf.apigamestore.sale_report.dto.RevenueProjection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SaleReportService {
    private final OrderRepository orderRepository;

    public List<RevenueProjection> getRevenue(LocalDateTime startDate, LocalDateTime endDate, String type) {
        return switch (type.toUpperCase()) {
            case "MONTH" -> orderRepository.getRevenueByMonth(startDate, endDate);
            case "WEEK" -> orderRepository.getRevenueByWeek(startDate, endDate);
            case "DAY" -> orderRepository.getRevenueByDay(startDate, endDate);
            default -> throw new IllegalStateException("Unexpected value: " + type.toUpperCase());
        };
    }
}
