-- Create notification tables
CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP NULL,
    email VARCHAR(255),
    phone_number VARCHAR(20),
    status VARCHAR(20) DEFAULT 'PENDING',
    error_message TEXT
);

-- Create indexes for notifications table
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_user_id_read ON notifications(user_id, is_read);
CREATE INDEX idx_notifications_status_created_at ON notifications(status, created_at);
CREATE INDEX idx_notifications_type_user_id ON notifications(type, user_id);

-- Create notification templates table (for future use)
CREATE TABLE notification_templates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(50) NOT NULL UNIQUE,
    title_template VARCHAR(255) NOT NULL,
    message_template TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create notification logs table (for audit trail)
CREATE TABLE notification_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    notification_id BIGINT,
    action VARCHAR(50) NOT NULL,
    details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (notification_id) REFERENCES notifications(id) ON DELETE CASCADE
);

-- Create indexes for notification_logs table
CREATE INDEX idx_notification_logs_notification_id ON notification_logs(notification_id);
CREATE INDEX idx_notification_logs_action ON notification_logs(action);

-- Insert default notification templates
INSERT INTO notification_templates (type, title_template, message_template) VALUES
('ORDER_CONFIRMATION', 'Order Confirmed', 'Your order {orderId} has been confirmed and is being processed.'),
('PAYMENT_SUCCESS', 'Payment Successful', 'Your payment of {amount} has been successfully processed.'),
('PAYMENT_FAILED', 'Payment Failed', 'Your payment could not be processed. Please try again.'),
('ORDER_SHIPPED', 'Order Shipped', 'Your order {orderId} has been shipped and is on its way.'),
('ORDER_DELIVERED', 'Order Delivered', 'Your order {orderId} has been delivered successfully.'),
('PASSWORD_RESET', 'Password Reset', 'Click the link to reset your password. This link expires in 24 hours.'),
('ACCOUNT_CREATED', 'Welcome!', 'Your account has been created successfully. Welcome to our platform!'),
('PROMOTION', 'Special Offer', 'Don''t miss out on our latest promotions and deals.'),
('SYSTEM_ALERT', 'System Alert', 'Important system notification.');
