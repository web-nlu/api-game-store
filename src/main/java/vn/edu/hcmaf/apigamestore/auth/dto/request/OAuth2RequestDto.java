package vn.edu.hcmaf.apigamestore.auth.dto.request;

import lombok.Data;

@Data
public class OAuth2RequestDto {
        private String code;
        private String redirectUri;

}
