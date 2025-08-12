package com.ecommerce.adapter.web.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateInventoryRequestDto {

    @JsonProperty("productId")
    @NotNull(message = "Product ID is required")
    private Long productId;

    @JsonProperty("quantity")
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @JsonProperty("operation")
    @NotNull(message = "Operation type is required")
    private InventoryOperation operation; // ADD, SUBTRACT, SET

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("warehouseId")
    private Long warehouseId;

    public enum InventoryOperation {
        ADD, SUBTRACT, SET
    }

    // Default constructor
    public UpdateInventoryRequestDto() {}

    // Constructor
    public UpdateInventoryRequestDto(Long productId, Integer quantity, InventoryOperation operation,
                                   String reason, Long warehouseId) {
        this.productId = productId;
        this.quantity = quantity;
        this.operation = operation;
        this.reason = reason;
        this.warehouseId = warehouseId;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public InventoryOperation getOperation() {
        return operation;
    }

    public void setOperation(InventoryOperation operation) {
        this.operation = operation;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }
}
