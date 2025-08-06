package com.ecommerce.core.usecase.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserNotificationsRequest {
    private Long userId;
    private Boolean unreadOnly;
    private Integer page;
    private Integer size;

    public static GetUserNotificationsRequest forUser(Long userId) {
        return GetUserNotificationsRequest.builder()
                .userId(userId)
                .unreadOnly(false)
                .page(0)
                .size(20)
                .build();
    }

    public static GetUserNotificationsRequest unreadForUser(Long userId) {
        return GetUserNotificationsRequest.builder()
                .userId(userId)
                .unreadOnly(true)
                .page(0)
                .size(20)
                .build();
    }
}