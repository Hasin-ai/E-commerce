-- Create payments table
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    payment_id VARCHAR(255) UNIQUE NOT NULL,
    order_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(255),
    gateway_response TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP,
    
    CONSTRAINT fk_payment_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- Create indexes for better performance
CREATE INDEX idx_payments_payment_id ON payments(payment_id);
CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_user_id ON payments(user_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_transaction_id ON payments(transaction_id);
CREATE INDEX idx_payments_created_at ON payments(created_at);

-- Create payment_methods table for storing user payment methods
CREATE TABLE payment_methods (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    provider_payment_method_id VARCHAR(255) NOT NULL,
    last_four VARCHAR(4),
    brand VARCHAR(50),
    exp_month INTEGER,
    exp_year INTEGER,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_payment_method_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create indexes for payment methods
CREATE INDEX idx_payment_methods_user_id ON payment_methods(user_id);
CREATE INDEX idx_payment_methods_provider_id ON payment_methods(provider_payment_method_id);

-- Create refunds table
CREATE TABLE refunds (
    id BIGSERIAL PRIMARY KEY,
    payment_id BIGINT NOT NULL,
    refund_id VARCHAR(255) UNIQUE NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    reason VARCHAR(500),
    status VARCHAR(50) NOT NULL,
    gateway_response TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP,
    
    CONSTRAINT fk_refund_payment FOREIGN KEY (payment_id) REFERENCES payments(id)
);

-- Create indexes for refunds
CREATE INDEX idx_refunds_payment_id ON refunds(payment_id);
CREATE INDEX idx_refunds_refund_id ON refunds(refund_id);
CREATE INDEX idx_refunds_status ON refunds(status);