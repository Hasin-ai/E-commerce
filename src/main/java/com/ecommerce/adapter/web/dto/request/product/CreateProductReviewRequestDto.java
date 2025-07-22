package com.ecommerce.adapter.web.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class CreateProductReviewRequestDto {

    @JsonProperty("userId")
    @NotNull(message = "User ID is required")
    private Long userId;

    @JsonProperty("productId")
    @NotNull(message = "Product ID is required")
    private Long productId;

    @JsonProperty("orderId")
    private Long orderId; // To verify purchase

    @JsonProperty("rating")
    @NotNull(message = "Rating is required")
    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private BigDecimal rating;

    @JsonProperty("title")
    @Size(max = 200, message = "Review title cannot exceed 200 characters")
    private String title;

    @JsonProperty("comment")
    @Size(max = 2000, message = "Review comment cannot exceed 2000 characters")
    private String comment;

    @JsonProperty("pros")
    private List<String> pros;

    @JsonProperty("cons")
    private List<String> cons;

    @JsonProperty("recommendProduct")
    private Boolean recommendProduct;

    @JsonProperty("imageUrls")
    private List<String> imageUrls;

    @JsonProperty("videoUrl")
    private String videoUrl;

    // Default constructor
    public CreateProductReviewRequestDto() {}

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getPros() {
        return pros;
    }

    public void setPros(List<String> pros) {
        this.pros = pros;
    }

    public List<String> getCons() {
        return cons;
    }

    public void setCons(List<String> cons) {
        this.cons = cons;
    }

    public Boolean getRecommendProduct() {
        return recommendProduct;
    }

    public void setRecommendProduct(Boolean recommendProduct) {
        this.recommendProduct = recommendProduct;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
