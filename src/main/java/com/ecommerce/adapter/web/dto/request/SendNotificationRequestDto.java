package com.ecommerce.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class SendNotificationRequestDto {

    @JsonProperty("recipientId")
    private Long recipientId; // Specific user ID, null for broadcast

    @JsonProperty("recipientIds")
    private List<Long> recipientIds; // Multiple users

    @JsonProperty("type")
    @NotBlank(message = "Notification type is required")
    private String type; // EMAIL, SMS, PUSH, IN_APP

    @JsonProperty("subject")
    private String subject; // For email notifications

    @JsonProperty("message")
    @NotBlank(message = "Message is required")
    private String message;

    @JsonProperty("template")
    private String template; // Template name for predefined notifications

    @JsonProperty("templateData")
    private Map<String, Object> templateData; // Data for template variables

    @JsonProperty("priority")
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, URGENT

    @JsonProperty("scheduleTime")
    private String scheduleTime; // ISO 8601 format for scheduled notifications

    @JsonProperty("metadata")
    private Map<String, Object> metadata; // Additional data

    // Default constructor
    public SendNotificationRequestDto() {}

    // Getters and Setters
    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public List<Long> getRecipientIds() {
        return recipientIds;
    }

    public void setRecipientIds(List<Long> recipientIds) {
        this.recipientIds = recipientIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, Object> getTemplateData() {
        return templateData;
    }

    public void setTemplateData(Map<String, Object> templateData) {
        this.templateData = templateData;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
