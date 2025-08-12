package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.ProcessPaymentRequestDto;
import com.ecommerce.adapter.web.dto.response.PaymentResponseDto;
import com.ecommerce.core.usecase.payment.ProcessPaymentUseCase;
import com.ecommerce.core.usecase.payment.ProcessPaymentRequest;
import com.ecommerce.core.usecase.payment.ProcessPaymentResponse;
import com.ecommerce.core.usecase.payment.GetPaymentUseCase;
import com.ecommerce.core.usecase.payment.GetPaymentRequest;
import com.ecommerce.core.usecase.payment.GetPaymentResponse;
import com.ecommerce.core.domain.user.repository.UserRepository;
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

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final GetPaymentUseCase getPaymentUseCase;
    private final UserRepository userRepository;

    public PaymentController(ProcessPaymentUseCase processPaymentUseCase, 
                           GetPaymentUseCase getPaymentUseCase,
                           UserRepository userRepository) {
        this.processPaymentUseCase = processPaymentUseCase;
        this.getPaymentUseCase = getPaymentUseCase;
        this.userRepository = userRepository;
    }

    @PostMapping("/process")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> processPayment(
            @Valid @RequestBody ProcessPaymentRequestDto requestDto,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);
        
        ProcessPaymentRequest request = new ProcessPaymentRequest(
            userId,
            requestDto.getOrderId(),
            requestDto.getAmount(),
            requestDto.getCurrency(),
            requestDto.getPaymentMethod(),
            requestDto.getPaymentMethodId(),
            requestDto.getCustomerId()
        );
        
        ProcessPaymentResponse response = processPaymentUseCase.execute(request);
        
        PaymentResponseDto responseDto = new PaymentResponseDto(
            response.getPaymentId(),
            requestDto.getOrderId(),
            requestDto.getAmount(),
            requestDto.getCurrency(),
            requestDto.getPaymentMethod(),
            response.getStatus(),
            response.getTransactionId(),
            null, // gatewayResponse not exposed in DTO
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now()
        );
        
        return ResponseEntity.ok(ApiResponse.success(responseDto, response.getMessage()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPayment(
            @PathVariable @NotNull @Positive Long id,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);
        
        GetPaymentRequest request = new GetPaymentRequest(id, userId);
        GetPaymentResponse response = getPaymentUseCase.execute(request);
        
        PaymentResponseDto responseDto = new PaymentResponseDto(
            response.getId(),
            response.getOrderId(),
            response.getAmount(),
            response.getCurrency(),
            response.getPaymentMethod(),
            response.getStatus(),
            response.getTransactionId(),
            null, // gatewayResponse not exposed in DTO
            response.getCreatedAt(),
            response.getProcessedAt()
        );
        
        return ResponseEntity.ok(ApiResponse.success(responseDto, "Payment retrieved successfully"));
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


    private Long getUserIdFromEmail(String email) {
        return userRepository.findByEmail(new com.ecommerce.core.domain.user.valueobject.Email(email))
            .orElseThrow(() -> new RuntimeException("User not found"))
            .getId();
    }
}