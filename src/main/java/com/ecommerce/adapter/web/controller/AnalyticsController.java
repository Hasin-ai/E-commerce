package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.TrackEventRequestDto;
import com.ecommerce.adapter.web.dto.request.TrackUserBehaviorRequestDto;
import com.ecommerce.adapter.web.dto.response.AnalyticsResponseDto;
import com.ecommerce.core.usecase.analytics.TrackEventUseCase;
import com.ecommerce.core.usecase.analytics.TrackEventRequest;
import com.ecommerce.core.usecase.analytics.TrackEventResponse;
import com.ecommerce.core.usecase.analytics.GetAnalyticsDashboardUseCase;
import com.ecommerce.core.usecase.analytics.GetAnalyticsDashboardResponse;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.shared.dto.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@Validated
@RequiredArgsConstructor
public class AnalyticsController {

    private final TrackEventUseCase trackEventUseCase;
    private final GetAnalyticsDashboardUseCase getAnalyticsDashboardUseCase;
    private final UserRepository userRepository;

    @PostMapping("/track")
    public ResponseEntity<ApiResponse<Void>> trackEvent(
            @Valid @RequestBody TrackEventRequestDto requestDto,
            HttpServletRequest request,
            Authentication authentication) {
        
        Long userId = null;
        if (authentication != null) {
            userId = getUserIdFromAuthentication(authentication);
        }
        
        TrackEventRequest trackRequest = TrackEventRequest.builder()
                .eventType(requestDto.getEventType())
                .userId(userId)
                .sessionId(request.getSession().getId())
                .properties(buildEventProperties(requestDto))
                .userAgent(request.getHeader("User-Agent"))
                .ipAddress(request.getRemoteAddr())
                .build();
        
        TrackEventResponse response = trackEventUseCase.execute(trackRequest);
        
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(ApiResponse.success(null, response.getMessage()));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage()));
        }
    }

    @PostMapping("/track/page-view")
    public ResponseEntity<ApiResponse<Void>> trackPageView(
            @RequestParam String page,
            @RequestParam(required = false) String referrer,
            Authentication authentication) {
        
        String userEmail = authentication != null ? authentication.getName() : null;
        
        // TODO: Implement page view tracking
        return ResponseEntity.ok(ApiResponse.success(null, "Page view tracked successfully"));
    }

    @PostMapping("/track/product-view")
    public ResponseEntity<ApiResponse<Void>> trackProductView(
            @RequestParam Long productId,
            @RequestParam(required = false) String source,
            Authentication authentication) {
        
        String userEmail = authentication != null ? authentication.getName() : null;
        
        // TODO: Implement product view tracking
        return ResponseEntity.ok(ApiResponse.success(null, "Product view tracked successfully"));
    }

    // Admin analytics endpoints
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AnalyticsResponseDto>> getDashboardAnalytics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        
        // Use default date range if not provided
        LocalDate from = startDate != null ? startDate : LocalDate.now().minusDays(30);
        LocalDate to = endDate != null ? endDate : LocalDate.now();
        
        GetAnalyticsDashboardResponse response = getAnalyticsDashboardUseCase.execute(from, to);
        
        AnalyticsResponseDto analyticsDto = AnalyticsResponseDto.builder()
                .totalRevenue(response.getDashboard().getTotalRevenue())
                .totalOrders(response.getDashboard().getTotalOrders())
                .totalUsers(response.getDashboard().getTotalCustomers())
                .conversionRate(response.getDashboard().getConversionRate() != null ? 
                    response.getDashboard().getConversionRate().doubleValue() : 0.0)
                .averageOrderValue(response.getDashboard().getAverageOrderValue())
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(analyticsDto, "Dashboard analytics retrieved successfully"));
    }

    @GetMapping("/admin/sales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSalesAnalytics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "day") String groupBy) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Sales analytics retrieved successfully"));
    }

    @GetMapping("/admin/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProductAnalytics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Product analytics retrieved successfully"));
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserAnalytics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "User analytics retrieved successfully"));
    }

    @GetMapping("/admin/conversion")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getConversionAnalytics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Conversion analytics retrieved successfully"));
    }

    @GetMapping("/admin/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRevenueAnalytics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "month") String period) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Revenue analytics retrieved successfully"));
    }

    // Helper methods
    private Long getUserIdFromAuthentication(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            return userRepository.findByEmail(new com.ecommerce.core.domain.user.valueobject.Email(userEmail))
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
        } catch (Exception e) {
            return null;
        }
    }
    
    private Map<String, Object> buildEventProperties(TrackEventRequestDto requestDto) {
        Map<String, Object> properties = new java.util.HashMap<>();
        
        if (requestDto.getProductId() != null) {
            properties.put("product_id", requestDto.getProductId());
        }
        if (requestDto.getOrderId() != null) {
            properties.put("order_id", requestDto.getOrderId());
        }
        if (requestDto.getCategory() != null) {
            properties.put("category", requestDto.getCategory());
        }
        if (requestDto.getPage() != null) {
            properties.put("page", requestDto.getPage());
        }
        if (requestDto.getReferrer() != null) {
            properties.put("referrer", requestDto.getReferrer());
        }
        if (requestDto.getMetadata() != null) {
            properties.putAll(requestDto.getMetadata());
        }
        
        return properties;
    }
}