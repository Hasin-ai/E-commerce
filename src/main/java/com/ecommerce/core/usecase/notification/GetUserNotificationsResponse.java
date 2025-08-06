package com.ecommerce.core.usecase.notification;

import com.ecommerce.core.domain.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserNotificationsResponse {
    private List<Notification> notifications;
    private Integer totalCount;
    private Integer unreadCount;
    private Integer page;
    private Integer size;
    private Integer totalPages;

    public static GetUserNotificationsResponse success(List<Notification> notifications, 
                                                     Integer totalCount, Integer unreadCount,
                                                     Integer page, Integer size) {
        return GetUserNotificationsResponse.builder()
                .notifications(notifications)
                .totalCount(totalCount)
                .unreadCount(unreadCount)
                .page(page)
                .size(size)
                .totalPages((int) Math.ceil((double) totalCount / size))
                .build();
    }
}