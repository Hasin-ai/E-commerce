package com.ecommerce.infrastructure.external.payment;

public class PaymentGatewayResponse {
    private boolean successful;
    private String transactionId;
    private String errorMessage;
    private String rawResponse;

    public PaymentGatewayResponse(boolean successful, String transactionId, String errorMessage, String rawResponse) {
        this.successful = successful;
        this.transactionId = transactionId;
        this.errorMessage = errorMessage;
        this.rawResponse = rawResponse;
    }

    public static PaymentGatewayResponse success(String transactionId, String rawResponse) {
        return new PaymentGatewayResponse(true, transactionId, null, rawResponse);
    }

    public static PaymentGatewayResponse failure(String errorMessage, String rawResponse) {
        return new PaymentGatewayResponse(false, null, errorMessage, rawResponse);
    }

    // Getters
    public boolean isSuccessful() { return successful; }
    public String getTransactionId() { return transactionId; }
    public String getErrorMessage() { return errorMessage; }
    public String getRawResponse() { return rawResponse; }
}