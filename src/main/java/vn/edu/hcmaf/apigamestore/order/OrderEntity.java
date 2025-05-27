package vn.edu.hcmaf.apigamestore.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import vn.edu.hcmaf.apigamestore.common.BaseEntity;
import vn.edu.hcmaf.apigamestore.user.UserEntity;

import java.util.List;

@Entity
@Data
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
    private String status; // e.g., "PENDING", "COMPLETED", "CANCELED"
    private String paymentMethod; // e.g., "CREDIT_CARD", "PAYPAL"
    private String paymentLinkId;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailEntity> orderDetails;

}
