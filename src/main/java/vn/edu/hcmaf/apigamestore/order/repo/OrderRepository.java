package vn.edu.hcmaf.apigamestore.order.repo;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import vn.edu.hcmaf.apigamestore.order.OrderEntity;
import vn.edu.hcmaf.apigamestore.order.dto.OrderUserDTO;

import vn.edu.hcmaf.apigamestore.sale_report.dto.RevenueProjection;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByUserId(Long id);

  @NativeQuery(value = """
    SELECT
      o.id,
      o.order_code,
      o.total_price,
      o.status,
      o.payment_method,
      o.payment_link_id,
      CAST(EXTRACT(EPOCH FROM o.created_at) AS bigint) AS created_at,
      u.id user_id,
      u.phone_number,
      u.email
    FROM orders o
    JOIN users u ON o.user_id = u.id
    WHERE (1 = 1)
    AND (:userId IS NULL OR o.user_id = :userId)
    AND (:status IS NULL OR LOWER(o.status) = LOWER(:status))
    AND (:createdAt IS NULL OR (
        o.created_at >= to_timestamp(:createdAt)
        AND o.created_at < to_timestamp(:createdAt + 86400)
      ))
    AND (
      (:search IS NULL OR CAST(o.id AS text) = :search)
      OR (:search IS NULL OR u.email = :search)
      OR (:search IS NULL OR u.phone_number = :search)
    )
    ORDER BY o.created_at DESC
    LIMIT :limit OFFSET :offset
    """)

  List<OrderUserDTO> filterOrders(
          @Param("createdAt") Long createdAt,
          @Param("userId") Long userId,
          @Param("search") String search,
          @Param("status") String status,
          @Param("limit") Integer limit,
          @Param("offset") Integer offset
  );

@NativeQuery(value = """
    SELECT
      CONCAT('Tháng ', TO_CHAR(DATE_TRUNC('month', created_at), 'MM/YYYY')) AS label,
      DATE_TRUNC('month', created_at) AS time,
      EXTRACT(EPOCH FROM DATE_TRUNC('month', created_at)) AS epoch,
      ROUND(SUM(total_price)::numeric, 2) AS totalRevenue
    FROM orders
    WHERE status = 'COMPLETED'
      AND is_deleted = false
      AND created_at BETWEEN :startDate AND :endDate
    GROUP BY DATE_TRUNC('month', created_at)
    ORDER BY DATE_TRUNC('month', created_at)
""")
List<RevenueProjection> getRevenueByMonth(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);




  @NativeQuery(value = """
    SELECT
      CONCAT('Tuần ', TO_CHAR(DATE_TRUNC('week', created_at), 'IW/YYYY')) AS label,
      DATE_TRUNC('week', created_at) AS time,
      EXTRACT(EPOCH FROM DATE_TRUNC('week', created_at)) AS epoch,
      ROUND(SUM(total_price)::numeric, 2) AS totalRevenue
    FROM orders
    WHERE status = 'COMPLETED'
      AND is_deleted = false
      AND created_at BETWEEN :startDate AND :endDate
    GROUP BY DATE_TRUNC('week', created_at)
    ORDER BY DATE_TRUNC('week', created_at)
    """)
  List<RevenueProjection> getRevenueByWeek(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

  @NativeQuery(value = """
    SELECT
      TO_CHAR(created_at, 'DD/MM/YYYY') AS label,
      DATE_TRUNC('day', created_at) AS time,
      EXTRACT(EPOCH FROM DATE_TRUNC('day', created_at)) AS epoch,
      ROUND(SUM(total_price)::numeric, 2) AS totalRevenue
    FROM orders
    WHERE status = 'COMPLETED'
      AND is_deleted = false
      AND created_at BETWEEN :startDate AND :endDate
    GROUP BY DATE_TRUNC('day', created_at), TO_CHAR(created_at, 'DD/MM/YYYY')
    ORDER BY DATE_TRUNC('day', created_at)
    """)
  List<RevenueProjection> getRevenueByDay(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);


}
