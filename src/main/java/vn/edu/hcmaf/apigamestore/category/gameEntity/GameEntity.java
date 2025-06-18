package vn.edu.hcmaf.apigamestore.category.gameEntity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.hcmaf.apigamestore.category.CategoryEntity;
import vn.edu.hcmaf.apigamestore.category.gameEntity.dto.GameResponseDto;
import vn.edu.hcmaf.apigamestore.common.BaseEntity;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // ví dụ: "Liên Minh Huyền Thoại", "PUBG", v.v.

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "game")
    private List<AccountEntity> accounts;

    //mapper

}