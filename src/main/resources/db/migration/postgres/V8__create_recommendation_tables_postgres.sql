-- PostgreSQL version of V8__create_recommendation_tables.sql
-- Create recommendation and search tables
CREATE TABLE user_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_preferences TEXT,
    brand_preferences TEXT,
    price_range_min DECIMAL(10,2),
    price_range_max DECIMAL(10,2),
    preferred_colors TEXT,
    preferred_sizes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE(user_id)
);

CREATE TABLE product_recommendations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    recommendation_type VARCHAR(50) NOT NULL,
    score DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

CREATE TABLE product_similarities (
    id BIGSERIAL PRIMARY KEY,
    product_a_id BIGINT NOT NULL,
    product_b_id BIGINT NOT NULL,
    similarity_score DECIMAL(5,4) NOT NULL DEFAULT 0.0000,
    similarity_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_a_id) REFERENCES products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (product_b_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE(product_a_id, product_b_id, similarity_type)
);

CREATE TABLE trending_products (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    category_id BIGINT,
    trend_score DECIMAL(8,4) NOT NULL DEFAULT 0.0000,
    views_count INTEGER DEFAULT 0,
    purchases_count INTEGER DEFAULT 0,
    date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    UNIQUE(product_id, date)
);

CREATE TABLE search_suggestions (
    id BIGSERIAL PRIMARY KEY,
    query VARCHAR(255) NOT NULL,
    suggestion VARCHAR(255) NOT NULL,
    frequency INTEGER DEFAULT 1,
    category VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(query, suggestion)
);

CREATE TABLE user_search_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    query VARCHAR(255) NOT NULL,
    results_count INTEGER DEFAULT 0,
    clicked_product_id BIGINT,
    search_filters TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (clicked_product_id) REFERENCES products(product_id)
);

-- Create indexes for better performance
CREATE INDEX idx_user_preferences_user_id ON user_preferences(user_id);
CREATE INDEX idx_product_recommendations_user_id ON product_recommendations(user_id, recommendation_type);
CREATE INDEX idx_product_recommendations_product_id ON product_recommendations(product_id);
CREATE INDEX idx_product_recommendations_score ON product_recommendations(score DESC);
CREATE INDEX idx_product_recommendations_expires_at ON product_recommendations(expires_at);

CREATE INDEX idx_product_similarities_product_a ON product_similarities(product_a_id, similarity_score DESC);
CREATE INDEX idx_product_similarities_product_b ON product_similarities(product_b_id, similarity_score DESC);

CREATE INDEX idx_trending_products_date_score ON trending_products(date, trend_score DESC);
CREATE INDEX idx_trending_products_category ON trending_products(category_id, trend_score DESC);

CREATE INDEX idx_search_suggestions_query ON search_suggestions(query);
CREATE INDEX idx_search_suggestions_frequency ON search_suggestions(frequency DESC);

CREATE INDEX idx_user_search_history_user_id ON user_search_history(user_id, created_at DESC);
