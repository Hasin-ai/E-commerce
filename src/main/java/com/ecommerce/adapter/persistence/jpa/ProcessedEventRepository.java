package com.ecommerce.adapter.persistence.jpa;

import com.ecommerce.adapter.persistence.common.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, String> {

    Optional<ProcessedEventEntity> findByEventId(String eventId);

    void deleteByProcessedAtBefore(LocalDateTime cutoff);
}
