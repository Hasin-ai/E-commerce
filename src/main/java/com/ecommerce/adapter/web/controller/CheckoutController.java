package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.CheckoutRequestDto;
import com.ecommerce.adapter.web.dto.response.CheckoutResponseDto;
import com.ecommerce.core.usecase.checkout.CheckoutWorkflowUseCase;
import com.ecommerce.core.usecase.checkout.CheckoutWorkflowRequest;
import com.ecommerce.core.usecase.checkout.CheckoutWorkflowResponse;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.shared.dto.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Checkout controller that handles the complete checkout workflow:
 * Cart -> Order -> Payment -> Completion
 */
@RestController
@RequestMapping("/api/checkout")
@Validated
@PreAuthorize("isAuthenticated()")
public class CheckoutController {

    private final CheckoutWorkflowUseCase checkoutWorkflowUseCase;
    private final UserRepository userRepository;

    public CheckoutController(CheckoutWorkflowUseCase checkoutWorkflowUseCase, UserRepository userRepository) {
        this.checkoutWorkflowUseCase = checkoutWorkflowUseCase;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CheckoutResponseDto>> checkout(
            @Valid @RequestBody CheckoutRequestDto requestDto,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);
        
        CheckoutWorkflowRequest request = new CheckoutWorkflowRequest(
            userId,
            new CheckoutWorkflowRequest.AddressRequest(
                requestDto.getShippingAddress().getStreet(),
                requestDto.getShippingAddress().getCity(),
                requestDto.getShippingAddress().getState(),
                requestDto.getShippingAddress().getZipCode(),
                requestDto.getShippingAddress().getCountry()
            ),
            new CheckoutWorkflowRequest.AddressRequest(
                requestDto.getBillingAddress().getStreet(),
                requestDto.getBillingAddress().getCity(),
                requestDto.getBillingAddress().getState(),
                requestDto.getBillingAddress().getZipCode(),
                requestDto.getBillingAddress().getCountry()
            ),
            requestDto.getPaymentMethod(),
            requestDto.getPaymentMethodId(),
            requestDto.getCustomerId()
        );
        
        CheckoutWorkflowResponse response = checkoutWorkflowUseCase.execute(request);
        
        CheckoutResponseDto responseDto = new CheckoutResponseDto(
            response.getOrderId(),
            response.getOrderNumber(),
            response.getPaymentId(),
            response.getPaymentReference(),
            response.getTransactionId(),
            response.getTotalAmount(),
            response.getCurrency(),
            response.getStatus()
        );
        
        return ResponseEntity.ok(ApiResponse.success(responseDto, response.getMessage()));
    }

    private Long getUserIdFromEmail(String email) {
        return userRepository.findByEmail(new com.ecommerce.core.domain.user.valueobject.Email(email))
            .orElseThrow(() -> new RuntimeException("User not found"))
            .getId();
    }
}