package vn.edu.hcmaf.apigamestore.common.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public abstract class BaseResponse {
    protected String status;
    protected String message;
    protected LocalDateTime timestamp = LocalDateTime.now();
}