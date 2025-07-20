package com.ecommerce.adapter.web.mapper;

import com.ecommerce.adapter.web.dto.request.payment.*;
import com.ecommerce.adapter.web.dto.response.payment.*;
import com.ecommerce.core.usecase.payment.CreatePaymentSessionUseCase;
import com.ecommerce.core.usecase.payment.ProcessPaymentUseCase;
import com.ecommerce.core.usecase.payment.VerifyPaymentUseCase;
import com.ecommerce.core.usecase.payment.HandleWebhookUseCase;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public CreatePaymentSessionUseCase.CreatePaymentSessionRequest toCreatePaymentSessionRequest(CreatePaymentSessionRequestDto dto) {
        return CreatePaymentSessionUseCase.CreatePaymentSessionRequest.builder()
                .orderId(dto.getOrderId())
                .successUrl(dto.getSuccessUrl())
                .cancelUrl(dto.getCancelUrl())
                .build();
    }

    public CreatePaymentSessionResponseDto toCreatePaymentSessionResponseDto(CreatePaymentSessionUseCase.CreatePaymentSessionResponse response) {
        return CreatePaymentSessionResponseDto.builder()
                .paymentId(response.getPaymentId())
                .sessionId(response.getSessionId())
                .sessionUrl(response.getSessionUrl())
                .build();
    }

    public ProcessPaymentUseCase.ProcessPaymentRequest toProcessPaymentRequest(ProcessPaymentRequestDto dto) {
        return ProcessPaymentUseCase.ProcessPaymentRequest.builder()
                .paymentIntentId(dto.getPaymentIntentId())
                .paymentMethod(dto.getPaymentMethod())
                .cardLastFour(dto.getCardLastFour())
                .cardBrand(dto.getCardBrand())
                .build();
    }

    public ProcessPaymentResponseDto toProcessPaymentResponseDto(ProcessPaymentUseCase.ProcessPaymentResponse response) {
        return ProcessPaymentResponseDto.builder()
                .successful(response.isSuccessful())
                .paymentId(response.getPaymentId())
                .orderId(response.getOrderId())
                .transactionId(response.getTransactionId())
                .message(response.getMessage())
                .build();
    }

    public VerifyPaymentUseCase.VerifyPaymentRequest toVerifyPaymentRequest(VerifyPaymentRequestDto dto) {
        return VerifyPaymentUseCase.VerifyPaymentRequest.builder()
                .paymentId(dto.getPaymentId())
                .paymentIntentId(dto.getPaymentIntentId())
                .build();
    }

    public VerifyPaymentResponseDto toVerifyPaymentResponseDto(VerifyPaymentUseCase.VerifyPaymentResponse response) {
        return VerifyPaymentResponseDto.builder()
                .paymentId(response.getPaymentId())
                .orderId(response.getOrderId())
                .status(response.getStatus())
                .amount(response.getAmount())
                .currency(response.getCurrency())
                .paymentIntentId(response.getPaymentIntentId())
                .build();
    }

    public HandleWebhookUseCase.HandleWebhookRequest toHandleWebhookRequest(WebhookRequestDto dto) {
        return HandleWebhookUseCase.HandleWebhookRequest.builder()
                .payload(dto.getPayload())
                .signature(dto.getSignature())
                .build();
    }

    public WebhookResponseDto toWebhookResponseDto(HandleWebhookUseCase.HandleWebhookResponse response) {
        return WebhookResponseDto.builder()
                .successful(response.isSuccessful())
                .paymentId(response.getPaymentId())
                .eventType(response.getEventType())
                .message(response.getMessage())
                .build();
    }
}

