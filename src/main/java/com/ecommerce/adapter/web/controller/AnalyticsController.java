package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.EventRequest;
import com.ecommerce.adapter.web.mapper.AnalyticsMapper;
import com.ecommerce.core.domain.analytics.entity.Report;
import com.ecommerce.core.usecase.analytics.GenerateReportUseCase;
import com.ecommerce.core.usecase.analytics.TrackUserActivityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final TrackUserActivityUseCase trackUserActivityUseCase;
    private final GenerateReportUseCase generateReportUseCase;
    private final AnalyticsMapper analyticsMapper;

    @PostMapping("/events")
    public ResponseEntity<Void> trackEvent(@Valid @RequestBody EventRequest eventRequest) {
        trackUserActivityUseCase.execute(analyticsMapper.toDomain(eventRequest));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reports/{reportType}")
    public ResponseEntity<Report> getReport(@PathVariable String reportType, @RequestParam Map<String, Object> params) {
        Report report = generateReportUseCase.execute(reportType, params);
        return ResponseEntity.ok(report);
    }
}
