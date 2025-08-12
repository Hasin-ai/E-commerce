package com.ecommerce.core.usecase.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationRequest {
    private String to;
    private String subject;
    private String message;
    private String templateId;
    private Map<String, Object> templateVariables;
    private boolean isHtml;
}