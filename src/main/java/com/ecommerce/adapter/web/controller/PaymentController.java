package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.ProcessPaymentRequestDto;
import com.ecommerce.adapter.web.dto.response.PaymentResponseDto;
import com.ecommerce.shared.dto.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/payments")
@Validated
@PreAuthorize("isAuthenticated()")
public class PaymentController {

    // TODO: Inject use cases when implemented
    // private final ProcessPaymentUseCase processPaymentUseCase;
    // private final GetPaymentUseCase getPaymentUseCase;
    // private final RefundPaymentUseCase refundPaymentUseCase;
    // private final GetPaymentMethodsUseCase getPaymentMethodsUseCase;

    @PostMapping("/process")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> processPayment(
            @Valid @RequestBody ProcessPaymentRequestDto requestDto,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Payment processed successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPayment(
            @PathVariable @NotNull @Positive Long id,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Payment retrieved successfully"));
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> refundPayment(
            @PathVariable @NotNull @Positive Long id,
            @RequestParam(required = false) String reason) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Payment refunded successfully"));
    }

    @GetMapping("/methods")
    public ResponseEntity<ApiResponse<Object>> getPaymentMethods() {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Payment methods retrieved successfully"));
    }

    @PostMapping("/webhook/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {
        
        // TODO: Implement webhook handling
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/webhook/paypal")
    public ResponseEntity<String> handlePayPalWebhook(
            @RequestBody String payload,
            @RequestHeader("PAYPAL-TRANSMISSION-ID") String transmissionId) {
        
        // TODO: Implement webhook handling
        return ResponseEntity.ok("OK");
    }
}