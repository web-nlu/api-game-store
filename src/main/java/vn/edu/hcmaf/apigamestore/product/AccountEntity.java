package vn.edu.hcmaf.apigamestore.product;

import jakarta.persistence.*;
import lombok.Data;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.common.BaseEntity;
import vn.edu.hcmaf.apigamestore.product.dto.AccountDto;
import vn.edu.hcmaf.apigamestore.review.ReviewEntity;

import java.util.List;

@Entity
@Data
@Table(name = "accounts")
public class AccountEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Double price;
    private Double salePrice;

    private String image;
    private String info;

    private String server;

    @ElementCollection
    private List<String> imageGallery;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    private List<String> features;

    private Integer level;
    private String status;
    private String warranty;

    private Integer viewCount;
    private Integer saleCount;

    @ElementCollection
    private List<String> tags;

    private Double rating;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviews;
    // mapper

}