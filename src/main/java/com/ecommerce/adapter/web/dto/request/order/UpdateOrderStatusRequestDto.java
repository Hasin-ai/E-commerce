package com.ecommerce.adapter.web.dto.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateOrderStatusRequestDto {

    @JsonProperty("orderId")
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @JsonProperty("status")
    @NotBlank(message = "Status is required")
    private String status; // PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED

    @JsonProperty("statusReason")
    private String statusReason;

    @JsonProperty("trackingNumber")
    private String trackingNumber;

    @JsonProperty("carrierName")
    private String carrierName;

    @JsonProperty("estimatedDeliveryDate")
    private String estimatedDeliveryDate; // ISO 8601 format

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("notifyCustomer")
    private Boolean notifyCustomer = true;

    // Default constructor
    public UpdateOrderStatusRequestDto() {}

    // Constructor
    public UpdateOrderStatusRequestDto(Long orderId, String status, String statusReason,
                                     String trackingNumber, String carrierName,
                                     String estimatedDeliveryDate, String notes, Boolean notifyCustomer) {
        this.orderId = orderId;
        this.status = status;
        this.statusReason = statusReason;
        this.trackingNumber = trackingNumber;
        this.carrierName = carrierName;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.notes = notes;
        this.notifyCustomer = notifyCustomer;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getNotifyCustomer() {
        return notifyCustomer;
    }

    public void setNotifyCustomer(Boolean notifyCustomer) {
        this.notifyCustomer = notifyCustomer;
    }
}
