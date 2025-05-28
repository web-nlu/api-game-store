package vn.edu.hcmaf.apigamestore.order.dto;

import lombok.Data;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;

@Data
public class OrderFilterRequestDto extends LazyLoadingRequestDto {
    private Long createdAt;
    private String search;
    private String status;
}
