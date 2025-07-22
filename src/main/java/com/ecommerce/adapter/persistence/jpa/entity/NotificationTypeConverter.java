package com.ecommerce.adapter.persistence.jpa.entity;

import com.ecommerce.core.domain.notification.entity.Notification;
import com.ecommerce.core.domain.notification.valueobject.NotificationType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NotificationTypeConverter implements AttributeConverter<NotificationType, String> {

    @Override
    public String convertToDatabaseColumn(NotificationType attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public NotificationType convertToEntityAttribute(String dbData) {
        return dbData != null ? NotificationType.valueOf(dbData) : null;
    }
}
