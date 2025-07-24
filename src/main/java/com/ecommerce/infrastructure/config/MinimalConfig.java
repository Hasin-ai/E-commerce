package com.ecommerce.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.mode", havingValue = "minimal", matchIfMissing = true)
public class MinimalConfig {
    // This configuration will be used to run the application in minimal mode
    // with only user management and notification features enabled
}