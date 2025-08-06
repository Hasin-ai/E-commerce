package com.ecommerce.infrastructure.service;

import com.ecommerce.adapter.persistence.common.ProcessedEventEntity;
import com.ecommerce.adapter.persistence.jpa.ProcessedEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class IdempotencyService {

    private final ProcessedEventRepository processedEventRepository;

    public IdempotencyService(ProcessedEventRepository processedEventRepository) {
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional(readOnly = true)
    public boolean isEventAlreadyProcessed(String eventId) {
        return processedEventRepository.findByEventId(eventId).isPresent();
    }

    @Transactional
    public void markEventAsProcessed(String eventId) {
        processedEventRepository.save(new ProcessedEventEntity(eventId, LocalDateTime.now()));
    }

    @Transactional
    public void cleanupOldProcessedEvents(LocalDateTime cutoff) {
        processedEventRepository.deleteByProcessedAtBefore(cutoff);
    }
}
