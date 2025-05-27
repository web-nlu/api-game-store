package vn.edu.hcmaf.apigamestore.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmaf.apigamestore.order.OrderDetailEntity;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {

    List<OrderDetailEntity> findAllByOrderId(Long id);
}
