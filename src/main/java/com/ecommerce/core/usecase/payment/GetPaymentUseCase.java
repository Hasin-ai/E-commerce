package com.ecommerce.core.usecase.payment;

import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.repository.PaymentRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.NotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetPaymentUseCase {

    private final PaymentRepository paymentRepository;

    public GetPaymentUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public GetPaymentResponse execute(GetPaymentRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
            .orElseThrow(() -> new NotFoundException("Payment not found"));

        // Verify payment belongs to user (security check)
        if (!payment.getUserId().equals(request.getUserId())) {
            throw new BusinessException("Payment does not belong to user");
        }

        return new GetPaymentResponse(
            payment.getId(),
            payment.getPaymentId(),
            payment.getOrderId(),
            payment.getAmount(),
            payment.getCurrency(),
            payment.getMethod().name(),
            payment.getStatus().name(),
            payment.getTransactionId(),
            payment.getCreatedAt(),
            payment.getProcessedAt()
        );
    }
}