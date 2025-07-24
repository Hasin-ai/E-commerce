package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.adapter.persistence.entity.AnalyticsEventEntity;
import com.ecommerce.core.domain.analytics.entity.AnalyticsEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class AnalyticsMapper {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Mapping(target = "properties", expression = "java(parseProperties(entity.getProperties()))")
    public abstract AnalyticsEvent toDomain(AnalyticsEventEntity entity);
    
    @Mapping(target = "properties", ignore = true)
    public abstract AnalyticsEventEntity toEntity(AnalyticsEvent event);
    
    protected Map<String, Object> parseProperties(String propertiesJson) {
        if (propertiesJson == null || propertiesJson.isEmpty()) {
            return new HashMap<>();
        }
        
        try {
            return objectMapper.readValue(propertiesJson, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            return new HashMap<>();
        }
    }
}