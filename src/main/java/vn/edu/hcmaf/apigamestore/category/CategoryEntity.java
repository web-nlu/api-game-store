package vn.edu.hcmaf.apigamestore.category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.hcmaf.apigamestore.category.dto.CategoryResponseDto;
import vn.edu.hcmaf.apigamestore.category.gameEntity.GameEntity;
import vn.edu.hcmaf.apigamestore.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // Ví dụ: "moba", "fps"

    private String name; // Tên hiển thị: "MOBA", "FPS/Bắn Súng"

    private String icon; // Emoji: 🎮, 🔫,...

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameEntity> games;

    // mapper
    public CategoryResponseDto toCategoryResponseDto(boolean includeGames) {
 CategoryResponseDto categoryResponseDto = CategoryResponseDto.builder()
                .id(this.id)
                .name(this.name)
                .icon(this.icon)
                .games(new ArrayList<>())
                .build();
        System.out.println("CategoryEntity.toCategoryResponseDto: " + categoryResponseDto);
        if (includeGames) {
            this.games.forEach(game -> {
                categoryResponseDto.getGames().add(game.toGameResponseDto(true));
            });
        }
        return categoryResponseDto;
    }
}