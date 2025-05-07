package vn.edu.hcmaf.apigamestore.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LazyLoadingResponseDto<T> {
    int page;
    int size;
    int totalPages;
    Long totalElements;
    T data;
}
