package vn.edu.hcmaf.apigamestore.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.hcmaf.apigamestore.common.BaseEntity;
import vn.edu.hcmaf.apigamestore.user.UserEntity;

import java.util.List;

@Entity(name = "Order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private int orderCode;
    @ManyToOne
    @JsonIgnore
    private UserEntity user;
    private Double totalPrice;
    private String status; // e.g., "PENDING", "COMPLETED", "CANCELLED"
    private String paymentMethod; // e.g., "CREDIT_CARD", "PAYPAL"
    private String paymentLinkId;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailEntity> orderDetails;
}
