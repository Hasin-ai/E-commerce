package com.ecommerce.core.usecase.analytics;

import com.ecommerce.core.domain.analytics.entity.Event;
import com.ecommerce.core.domain.analytics.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackUserActivityUseCase {
    private final AnalyticsRepository analyticsRepository;

    public void execute(Event event) {
        analyticsRepository.saveEvent(event);
    }
}
