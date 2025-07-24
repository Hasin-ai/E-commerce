package com.ecommerce.core.usecase.payment;

public class ProcessPaymentResponse {
    private Long paymentId;
    private String paymentReference;
    private String status;
    private String transactionId;
    private String message;

    public ProcessPaymentResponse(Long paymentId, String paymentReference, String status, 
                                String transactionId, String message) {
        this.paymentId = paymentId;
        this.paymentReference = paymentReference;
        this.status = status;
        this.transactionId = transactionId;
        this.message = message;
    }

    // Getters
    public Long getPaymentId() { return paymentId; }
    public String getPaymentReference() { return paymentReference; }
    public String getStatus() { return status; }
    public String getTransactionId() { return transactionId; }
    public String getMessage() { return message; }
}