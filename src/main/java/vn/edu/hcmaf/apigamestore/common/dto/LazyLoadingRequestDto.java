package vn.edu.hcmaf.apigamestore.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class LazyLoadingRequestDto<T> {
    //validation
    @NotNull(message = "Page is required")
    private int page;
    @NotNull(message = "Size is required")
    private int size;
    private T sortBy;
}
