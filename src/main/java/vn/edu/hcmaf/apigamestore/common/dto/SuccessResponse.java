package vn.edu.hcmaf.apigamestore.common.dto;

import lombok.Data;

@Data
public class SuccessResponse<T> extends BaseResponse {
    private T data;

    public SuccessResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
