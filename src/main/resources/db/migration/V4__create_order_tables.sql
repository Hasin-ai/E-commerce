-- Create orders table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(255) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    subtotal_amount DECIMAL(19,2) NOT NULL,
    tax_amount DECIMAL(19,2) NOT NULL,
    shipping_amount DECIMAL(19,2) NOT NULL,
    discount_amount DECIMAL(19,2) DEFAULT 0,
    total_amount DECIMAL(19,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    tracking_number VARCHAR(255),
    estimated_delivery TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create order_items table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_sku VARCHAR(100),
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(19,2) NOT NULL,
    total_price DECIMAL(19,2) NOT NULL,
    
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Create order_addresses table
CREATE TABLE order_addresses (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    address_type VARCHAR(20) NOT NULL, -- 'SHIPPING' or 'BILLING'
    street VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL,
    
    CONSTRAINT fk_order_address_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_orders_order_number ON orders(order_number);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);

CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);

CREATE INDEX idx_order_addresses_order_id ON order_addresses(order_id);
CREATE INDEX idx_order_addresses_type ON order_addresses(address_type);