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

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByUserId(Long id);

  @NativeQuery(value =
          """
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
}
