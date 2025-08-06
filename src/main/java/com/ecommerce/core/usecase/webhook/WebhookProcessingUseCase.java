package com.ecommerce.core.usecase.webhook;

import com.ecommerce.core.usecase.payment.ProcessWebhookEventRequest;

public interface WebhookProcessingUseCase {
    void execute(ProcessWebhookEventRequest request);
}
