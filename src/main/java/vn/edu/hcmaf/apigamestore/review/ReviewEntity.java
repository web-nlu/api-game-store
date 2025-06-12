package vn.edu.hcmaf.apigamestore.review;

import jakarta.persistence.*;
import lombok.Data;
import vn.edu.hcmaf.apigamestore.common.BaseEntity;
import vn.edu.hcmaf.apigamestore.order.OrderEntity;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.user.UserEntity;

@Entity
@Table(name = "reviews")
@Data
public class ReviewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;
    private Integer stars;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private Long OldReviewId;
    private Boolean isHide;

}