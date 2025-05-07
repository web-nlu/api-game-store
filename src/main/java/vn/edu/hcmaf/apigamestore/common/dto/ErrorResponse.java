package vn.edu.hcmaf.apigamestore.common.dto;

import lombok.Data;

@Data
public class ErrorResponse extends BaseResponse {
    private String errorCode;

    public ErrorResponse(String errorCode,String status, String message) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }


}
