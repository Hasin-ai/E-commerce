package com.ecommerce.core.domain.payment.repository;

import com.ecommerce.core.domain.payment.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Transaction entities.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByExternalTransactionId(String externalTransactionId);

    List<Transaction> findByPaymentIdOrderByCreatedAtDesc(Long paymentId);

    List<Transaction> findByStatusAndCreatedAtBefore(Transaction.TransactionStatus status, LocalDateTime dateTime);
}
