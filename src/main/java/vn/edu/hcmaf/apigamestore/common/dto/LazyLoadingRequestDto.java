package vn.edu.hcmaf.apigamestore.common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class LazyLoadingRequestDto {
    //validation
    @NotNull(message = "Page is required")
    protected Integer page;
    @NotNull(message = "Size is required")
    protected Integer size;

}
