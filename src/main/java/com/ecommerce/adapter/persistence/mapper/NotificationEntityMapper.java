package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.core.domain.notification.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationEntityMapper {

    public Notification toJpaEntity(Notification notification) {
        // Since Notification entity already has JPA annotations,
        // we can return it directly
        return notification;
    }

    public Notification toDomainEntity(Notification jpaEntity) {
        // Since Notification entity already has JPA annotations,
        // we can return it directly
        return jpaEntity;
    }
}
