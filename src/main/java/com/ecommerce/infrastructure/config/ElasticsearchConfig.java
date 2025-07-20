package com.ecommerce.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
@EnableElasticsearchRepositories(basePackages = "com.ecommerce.adapter.persistence.elasticsearch.repository")
public class ElasticsearchConfig {
    // This configuration will only be active when Elasticsearch is enabled
}
