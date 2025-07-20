package com.ecommerce.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ecommerce.stripe")
@Data
public class StripeConfig {
    private String publicKey;
    private String secretKey;
    private String webhookSecret;
    private String successUrl;
    private String cancelUrl;
}
