package com.ecommerce.adapter.web.dto.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class InventoryResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("productId")
    private Long productId;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("availableQuantity")
    private Integer availableQuantity;

    @JsonProperty("reservedQuantity")
    private Integer reservedQuantity;

    @JsonProperty("totalQuantity")
    private Integer totalQuantity;

    @JsonProperty("reorderLevel")
    private Integer reorderLevel;

    @JsonProperty("maxStockLevel")
    private Integer maxStockLevel;

    @JsonProperty("warehouseId")
    private Long warehouseId;

    @JsonProperty("warehouseName")
    private String warehouseName;

    @JsonProperty("lastUpdated")
    private LocalDateTime lastUpdated;

    @JsonProperty("lastStockMovement")
    private LocalDateTime lastStockMovement;

    @JsonProperty("isLowStock")
    private Boolean isLowStock;

    @JsonProperty("isOutOfStock")
    private Boolean isOutOfStock;

    // Default constructor
    public InventoryResponseDto() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Integer getMaxStockLevel() {
        return maxStockLevel;
    }

    public void setMaxStockLevel(Integer maxStockLevel) {
        this.maxStockLevel = maxStockLevel;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getLastStockMovement() {
        return lastStockMovement;
    }

    public void setLastStockMovement(LocalDateTime lastStockMovement) {
        this.lastStockMovement = lastStockMovement;
    }

    public Boolean getIsLowStock() {
        return isLowStock;
    }

    public void setIsLowStock(Boolean isLowStock) {
        this.isLowStock = isLowStock;
    }

    public Boolean getIsOutOfStock() {
        return isOutOfStock;
    }

    public void setIsOutOfStock(Boolean isOutOfStock) {
        this.isOutOfStock = isOutOfStock;
    }
}
