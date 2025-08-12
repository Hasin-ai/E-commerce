-- PostgreSQL version of V10__create_vendor_tables.sql
-- Create vendors table
CREATE TABLE vendors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(50),
    website VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    commission_rate DECIMAL(5,4) DEFAULT 0.0000,
    business_license VARCHAR(255),
    tax_id VARCHAR(255),
    street VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    zip_code VARCHAR(20),
    country VARCHAR(100),
    account_number VARCHAR(255),
    routing_number VARCHAR(255),
    bank_name VARCHAR(255),
    account_holder_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_at TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_vendors_status ON vendors(status);
CREATE INDEX idx_vendors_email ON vendors(email);
CREATE INDEX idx_vendors_slug ON vendors(slug);

-- Add vendor_id to products table
ALTER TABLE products ADD COLUMN vendor_id BIGINT;
ALTER TABLE products ADD CONSTRAINT fk_products_vendor FOREIGN KEY (vendor_id) REFERENCES vendors(id);
CREATE INDEX idx_products_vendor_id ON products(vendor_id);
