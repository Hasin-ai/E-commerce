-- Insert sample categories
INSERT INTO categories (id, name, slug, description, is_active, sort_order) VALUES
(1, 'Electronics', 'electronics', 'Electronic devices and accessories', true, 1),
(2, 'Gaming', 'gaming', 'Gaming equipment and accessories', true, 2);

-- Insert sample products
INSERT INTO products (product_id, name, slug, description, sku, base_price, currency, category_id, stock_quantity, is_active, is_featured) VALUES
(1, 'Gaming Laptop', 'gaming-laptop', 'High-performance gaming laptop with RTX graphics', 'LAPTOP-001', 1299.99, 'USD', 1, 10, true, true),
(2, 'Gaming Mouse', 'gaming-mouse', 'RGB gaming mouse with high DPI', 'MOUSE-001', 79.99, 'USD', 1, 25, true, false),
(3, 'Mechanical Keyboard', 'mechanical-keyboard', 'RGB mechanical keyboard with blue switches', 'KEYBOARD-001', 149.99, 'USD', 1, 15, true, false);

-- Insert sample product images
INSERT INTO product_images (product_id, image_url, alt_text, is_primary, sort_order) VALUES
(1, '/images/gaming-laptop.jpg', 'Gaming Laptop', true, 1),
(2, '/images/gaming-mouse.jpg', 'Gaming Mouse', true, 1),
(3, '/images/mechanical-keyboard.jpg', 'Mechanical Keyboard', true, 1);