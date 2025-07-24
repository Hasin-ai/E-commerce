package com.ecommerce.adapter.web.controller;

import com.ecommerce.shared.dto.ApiResponse;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final HealthIndicator dbHealthIndicator;

    public HealthController(HealthIndicator dbHealthIndicator) {
        this.dbHealthIndicator = dbHealthIndicator;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("message", "E-commerce application is running");
        healthStatus.put("timestamp", LocalDateTime.now());
        healthStatus.put("version", "1.0.0");
        
        // Check database health
        Health dbHealth = dbHealthIndicator.health();
        healthStatus.put("database", dbHealth.getStatus().getCode());
        
        return ResponseEntity.ok(healthStatus);
    }

    @GetMapping("/health/detailed")
    public ResponseEntity<ApiResponse<Map<String, Object>>> detailedHealth() {
        Map<String, Object> healthDetails = new HashMap<>();
        
        // Application info
        Map<String, Object> appInfo = new HashMap<>();
        appInfo.put("name", "E-commerce Backend");
        appInfo.put("version", "1.0.0");
        appInfo.put("environment", System.getProperty("spring.profiles.active", "default"));
        appInfo.put("uptime", getUptime());
        healthDetails.put("application", appInfo);
        
        // Database health
        Health dbHealth = dbHealthIndicator.health();
        Map<String, Object> dbInfo = new HashMap<>();
        dbInfo.put("status", dbHealth.getStatus().getCode());
        dbInfo.put("details", dbHealth.getDetails());
        healthDetails.put("database", dbInfo);
        
        // System info
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("javaVersion", System.getProperty("java.version"));
        systemInfo.put("osName", System.getProperty("os.name"));
        systemInfo.put("osVersion", System.getProperty("os.version"));
        systemInfo.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        systemInfo.put("maxMemory", Runtime.getRuntime().maxMemory());
        systemInfo.put("totalMemory", Runtime.getRuntime().totalMemory());
        systemInfo.put("freeMemory", Runtime.getRuntime().freeMemory());
        healthDetails.put("system", systemInfo);
        
        return ResponseEntity.ok(ApiResponse.success(healthDetails, "Detailed health check completed"));
    }

    @GetMapping("/health/ready")
    public ResponseEntity<Map<String, Object>> readiness() {
        Map<String, Object> readinessStatus = new HashMap<>();
        
        // Check if application is ready to serve requests
        boolean isReady = true;
        
        // Check database connectivity
        Health dbHealth = dbHealthIndicator.health();
        if (!dbHealth.getStatus().getCode().equals("UP")) {
            isReady = false;
        }
        
        readinessStatus.put("status", isReady ? "READY" : "NOT_READY");
        readinessStatus.put("timestamp", LocalDateTime.now());
        readinessStatus.put("checks", Map.of(
            "database", dbHealth.getStatus().getCode()
        ));
        
        return ResponseEntity.ok(readinessStatus);
    }

    @GetMapping("/health/live")
    public ResponseEntity<Map<String, Object>> liveness() {
        Map<String, Object> livenessStatus = new HashMap<>();
        livenessStatus.put("status", "ALIVE");
        livenessStatus.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(livenessStatus);
    }

    private String getUptime() {
        long uptimeMillis = System.currentTimeMillis() - 
            java.lang.management.ManagementFactory.getRuntimeMXBean().getStartTime();
        long uptimeSeconds = uptimeMillis / 1000;
        long hours = uptimeSeconds / 3600;
        long minutes = (uptimeSeconds % 3600) / 60;
        long seconds = uptimeSeconds % 60;
        
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}