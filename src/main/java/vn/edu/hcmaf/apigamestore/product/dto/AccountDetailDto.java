package vn.edu.hcmaf.apigamestore.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.hcmaf.apigamestore.review.dto.ReviewDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailDto {
    private Long id;
    private String title;
    private Double price;
    private Double salePrice;
    private String category;
    private String image;
    private String info;
    private String game;
    private String server;
    private List<String> imageGallery;
    private String description;
    private List<String> features;
    private Integer level;
    private String status;
    private String warranty;
    private Long createdAt;
    private Long updatedAt;
    private Integer viewCount;
    private Integer saleCount;
    private List<String> tags;
    private Double rating;
    private List<ReviewDto> reviews;
}
