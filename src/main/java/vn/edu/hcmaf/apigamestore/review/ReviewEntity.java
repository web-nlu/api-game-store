package vn.edu.hcmaf.apigamestore.review;

import jakarta.persistence.*;
import lombok.Data;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;

@Entity
@Table(name = "reviews")
@Data
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String comment;
    private Integer stars;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;
}