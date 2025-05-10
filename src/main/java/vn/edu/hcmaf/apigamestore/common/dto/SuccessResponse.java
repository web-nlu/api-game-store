package vn.edu.hcmaf.apigamestore.common.dto;

import lombok.Data;

@Data
public class SuccessResponse<T> extends BaseResponse {
    private T data;

    public SuccessResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

}
