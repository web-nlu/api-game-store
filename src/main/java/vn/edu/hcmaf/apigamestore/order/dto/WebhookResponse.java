package vn.edu.hcmaf.apigamestore.order.dto;

import lombok.Data;

@Data
public class WebhookResponse {
    private String code;
    private String desc;
    private boolean success;
    private DataField data;
    private String signature;

    @Data
    public static class DataField {
        private int orderCode;
        private int amount;
        private String description;
        private String accountNumber;
        private String reference;
        private String transactionDateTime;
        private String currency;
        private String paymentLinkId;
        private String code;
        private String desc;
        private String counterAccountBankId;
        private String counterAccountBankName;
        private String counterAccountName;
        private String counterAccountNumber;
        private String virtualAccountName;
        private String virtualAccountNumber;
    }
}
