-- Insert sample categories
INSERT INTO categories (id, name, slug, description, is_active, sort_order) VALUES
(1, 'Electronics', 'electronics', 'Electronic devices and accessories', true, 1),
(2, 'Gaming', 'gaming', 'Gaming equipment and accessories', true, 2),
(3, 'Mobile Phones', 'mobile-phones', 'Smartphones and mobile devices', true, 3),
(4, 'Cameras', 'cameras', 'Digital cameras and photography equipment', true, 4),
(5, 'Audio', 'audio', 'Headphones, speakers and audio equipment', true, 5),
(6, 'Wearables', 'wearables', 'Smartwatches and fitness trackers', true, 6),
(7, 'Laptops', 'laptops', 'Laptops and portable computers', true, 7),
(8, 'Tablets', 'tablets', 'Tablets and e-readers', true, 8),
(9, 'Home Appliances', 'home-appliances', 'Smart home devices and appliances', true, 9),
(10, 'Accessories', 'accessories', 'Tech accessories and peripherals', true, 10);

-- Insert sample products
INSERT INTO products (product_id, name, slug, description, sku, base_price, currency, category_id, stock_quantity, is_active, is_featured) VALUES
-- Gaming Products
(1, 'Gaming Laptop', 'gaming-laptop', 'High-performance gaming laptop with RTX 4080 graphics', 'LAPTOP-001', 1299.99, 'USD', 2, 10, true, true),
(2, 'Gaming Mouse', 'gaming-mouse', 'RGB gaming mouse with 16000 DPI sensor', 'MOUSE-001', 79.99, 'USD', 2, 25, true, false),
(3, 'Mechanical Keyboard', 'mechanical-keyboard', 'RGB mechanical keyboard with blue switches', 'KEYBOARD-001', 149.99, 'USD', 2, 15, true, false),

-- Mobile Phones
(4, 'iPhone 15 Pro', 'iphone-15-pro', 'Latest iPhone with A17 Pro chip and titanium design', 'PHONE-001', 999.99, 'USD', 3, 50, true, true),
(5, 'Samsung Galaxy S24 Ultra', 'samsung-galaxy-s24-ultra', 'Premium Android phone with S Pen and 200MP camera', 'PHONE-002', 1199.99, 'USD', 3, 35, true, true),
(6, 'Google Pixel 8', 'google-pixel-8', 'AI-powered Android phone with advanced photography', 'PHONE-003', 699.99, 'USD', 3, 40, true, false),
(7, 'OnePlus 12', 'oneplus-12', 'Flagship Android phone with 120Hz display', 'PHONE-004', 799.99, 'USD', 3, 30, true, false),

-- Cameras
(8, 'Canon EOS R5', 'canon-eos-r5', 'Professional mirrorless camera with 45MP sensor', 'CAM-001', 3899.99, 'USD', 4, 8, true, true),
(9, 'Sony A7 IV', 'sony-a7-iv', 'Full-frame mirrorless camera for professionals', 'CAM-002', 2499.99, 'USD', 4, 12, true, true),
(10, 'Fujifilm X-T5', 'fujifilm-x-t5', 'APS-C mirrorless camera with vintage design', 'CAM-003', 1699.99, 'USD', 4, 15, true, false),
(11, 'DJI Action 4', 'dji-action-4', '4K action camera with stabilization', 'CAM-004', 399.99, 'USD', 4, 25, true, false),

-- Audio Equipment
(12, 'Sony WH-1000XM5', 'sony-wh-1000xm5', 'Premium noise-canceling wireless headphones', 'AUDIO-001', 399.99, 'USD', 5, 40, true, true),
(13, 'Apple AirPods Pro 2', 'airpods-pro-2', 'Wireless earbuds with adaptive transparency', 'AUDIO-002', 249.99, 'USD', 5, 60, true, true),
(14, 'Bose QuietComfort 45', 'bose-quietcomfort-45', 'Wireless noise-canceling headphones', 'AUDIO-003', 329.99, 'USD', 5, 35, true, false),
(15, 'JBL Flip 6', 'jbl-flip-6', 'Portable waterproof Bluetooth speaker', 'AUDIO-004', 129.99, 'USD', 5, 50, true, false),
(16, 'Audio-Technica ATH-M50x', 'audio-technica-ath-m50x', 'Professional studio monitor headphones', 'AUDIO-005', 149.99, 'USD', 5, 30, true, false),

-- Wearables
(17, 'Apple Watch Series 9', 'apple-watch-series-9', 'Advanced smartwatch with health monitoring', 'WEAR-001', 399.99, 'USD', 6, 45, true, true),
(18, 'Samsung Galaxy Watch 6', 'samsung-galaxy-watch-6', 'Android smartwatch with fitness tracking', 'WEAR-002', 329.99, 'USD', 6, 38, true, true),
(19, 'Fitbit Charge 6', 'fitbit-charge-6', 'Advanced fitness tracker with GPS', 'WEAR-003', 159.99, 'USD', 6, 55, true, false),
(20, 'Garmin Fenix 7', 'garmin-fenix-7', 'Rugged GPS smartwatch for outdoor activities', 'WEAR-004', 699.99, 'USD', 6, 20, true, false),

-- Laptops
(21, 'MacBook Pro 16-inch', 'macbook-pro-16', 'Professional laptop with M3 Pro chip', 'LAPTOP-002', 2499.99, 'USD', 7, 18, true, true),
(22, 'Dell XPS 13', 'dell-xps-13', 'Ultra-portable Windows laptop', 'LAPTOP-003', 1199.99, 'USD', 7, 25, true, true),
(23, 'ThinkPad X1 Carbon', 'thinkpad-x1-carbon', 'Business laptop with carbon fiber design', 'LAPTOP-004', 1899.99, 'USD', 7, 20, true, false),
(24, 'HP Spectre x360', 'hp-spectre-x360', '2-in-1 convertible laptop with OLED display', 'LAPTOP-005', 1399.99, 'USD', 7, 22, true, false),

-- Tablets
(25, 'iPad Pro 12.9-inch', 'ipad-pro-12-9', 'Professional tablet with M2 chip', 'TABLET-001', 1099.99, 'USD', 8, 30, true, true),
(26, 'Samsung Galaxy Tab S9', 'samsung-galaxy-tab-s9', 'Android tablet with S Pen included', 'TABLET-002', 799.99, 'USD', 8, 25, true, false),
(27, 'Microsoft Surface Pro 9', 'microsoft-surface-pro-9', 'Windows tablet with laptop performance', 'TABLET-003', 999.99, 'USD', 8, 20, true, false),

-- Home Appliances
(28, 'Amazon Echo Dot 5th Gen', 'amazon-echo-dot-5', 'Smart speaker with Alexa', 'HOME-001', 49.99, 'USD', 9, 80, true, false),
(29, 'Google Nest Hub 2nd Gen', 'google-nest-hub-2', 'Smart display with Google Assistant', 'HOME-002', 99.99, 'USD', 9, 45, true, false),
(30, 'Philips Hue Starter Kit', 'philips-hue-starter', 'Smart LED lighting system', 'HOME-003', 199.99, 'USD', 9, 35, true, false),
(31, 'Ring Video Doorbell 4', 'ring-video-doorbell-4', 'Smart doorbell with HD video', 'HOME-004', 199.99, 'USD', 9, 40, true, false),

-- Accessories
(32, 'Anker PowerBank 20000mAh', 'anker-powerbank-20000', 'High-capacity portable charger', 'ACC-001', 49.99, 'USD', 10, 60, true, false),
(33, 'Logitech MX Master 3S', 'logitech-mx-master-3s', 'Professional wireless mouse', 'ACC-002', 99.99, 'USD', 10, 40, true, false),
(34, 'USB-C Hub 7-in-1', 'usb-c-hub-7in1', 'Multi-port USB-C adapter', 'ACC-003', 39.99, 'USD', 10, 70, true, false),
(35, 'Wireless Charging Pad', 'wireless-charging-pad', '15W fast wireless charger', 'ACC-004', 29.99, 'USD', 10, 85, true, false);

-- Insert sample product images
INSERT INTO product_images (product_id, image_url, alt_text, is_primary, sort_order) VALUES
-- Gaming Products
(1, '/images/gaming-laptop.jpg', 'Gaming Laptop', true, 1),
(1, '/images/gaming-laptop-2.jpg', 'Gaming Laptop Side View', false, 2),
(2, '/images/gaming-mouse.jpg', 'Gaming Mouse', true, 1),
(3, '/images/mechanical-keyboard.jpg', 'Mechanical Keyboard', true, 1),

-- Mobile Phones
(4, '/images/iphone-15-pro.jpg', 'iPhone 15 Pro', true, 1),
(4, '/images/iphone-15-pro-back.jpg', 'iPhone 15 Pro Back', false, 2),
(5, '/images/samsung-galaxy-s24-ultra.jpg', 'Samsung Galaxy S24 Ultra', true, 1),
(5, '/images/samsung-galaxy-s24-ultra-spen.jpg', 'Galaxy S24 Ultra with S Pen', false, 2),
(6, '/images/google-pixel-8.jpg', 'Google Pixel 8', true, 1),
(7, '/images/oneplus-12.jpg', 'OnePlus 12', true, 1),

-- Cameras
(8, '/images/canon-eos-r5.jpg', 'Canon EOS R5', true, 1),
(8, '/images/canon-eos-r5-lens.jpg', 'Canon EOS R5 with Lens', false, 2),
(9, '/images/sony-a7-iv.jpg', 'Sony A7 IV', true, 1),
(10, '/images/fujifilm-x-t5.jpg', 'Fujifilm X-T5', true, 1),
(11, '/images/dji-action-4.jpg', 'DJI Action 4', true, 1),

-- Audio Equipment
(12, '/images/sony-wh-1000xm5.jpg', 'Sony WH-1000XM5', true, 1),
(13, '/images/airpods-pro-2.jpg', 'Apple AirPods Pro 2', true, 1),
(14, '/images/bose-quietcomfort-45.jpg', 'Bose QuietComfort 45', true, 1),
(15, '/images/jbl-flip-6.jpg', 'JBL Flip 6', true, 1),
(16, '/images/audio-technica-ath-m50x.jpg', 'Audio-Technica ATH-M50x', true, 1),

-- Wearables
(17, '/images/apple-watch-series-9.jpg', 'Apple Watch Series 9', true, 1),
(18, '/images/samsung-galaxy-watch-6.jpg', 'Samsung Galaxy Watch 6', true, 1),
(19, '/images/fitbit-charge-6.jpg', 'Fitbit Charge 6', true, 1),
(20, '/images/garmin-fenix-7.jpg', 'Garmin Fenix 7', true, 1),

-- Laptops
(21, '/images/macbook-pro-16.jpg', 'MacBook Pro 16-inch', true, 1),
(22, '/images/dell-xps-13.jpg', 'Dell XPS 13', true, 1),
(23, '/images/thinkpad-x1-carbon.jpg', 'ThinkPad X1 Carbon', true, 1),
(24, '/images/hp-spectre-x360.jpg', 'HP Spectre x360', true, 1),

-- Tablets
(25, '/images/ipad-pro-12-9.jpg', 'iPad Pro 12.9-inch', true, 1),
(26, '/images/samsung-galaxy-tab-s9.jpg', 'Samsung Galaxy Tab S9', true, 1),
(27, '/images/microsoft-surface-pro-9.jpg', 'Microsoft Surface Pro 9', true, 1),

-- Home Appliances
(28, '/images/amazon-echo-dot-5.jpg', 'Amazon Echo Dot 5th Gen', true, 1),
(29, '/images/google-nest-hub-2.jpg', 'Google Nest Hub 2nd Gen', true, 1),
(30, '/images/philips-hue-starter.jpg', 'Philips Hue Starter Kit', true, 1),
(31, '/images/ring-video-doorbell-4.jpg', 'Ring Video Doorbell 4', true, 1),

-- Accessories
(32, '/images/anker-powerbank.jpg', 'Anker PowerBank 20000mAh', true, 1),
(33, '/images/logitech-mx-master-3s.jpg', 'Logitech MX Master 3S', true, 1),
(34, '/images/usb-c-hub.jpg', 'USB-C Hub 7-in-1', true, 1),
(35, '/images/wireless-charging-pad.jpg', 'Wireless Charging Pad', true, 1);
