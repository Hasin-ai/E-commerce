#!/bin/bash

# Product Catalog Module Testing Script
BASE_URL="http://localhost:8080/api"
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}=== TESTING PRODUCT CATALOG MODULE ===${NC}"

# First, get authentication token
echo "Getting authentication token..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{
        "email": "john.doe@example.com",
        "password": "Password123!"
    }')

USER_TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)

if [ -z "$USER_TOKEN" ]; then
    echo -e "${RED}❌ Failed to get authentication token${NC}"
    exit 1
fi

# Test Category Creation
echo "Testing category creation..."
CATEGORY_RESPONSE=$(curl -s -X POST "$BASE_URL/products/categories" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $USER_TOKEN" \
    -d '{
        "name": "Electronics",
        "slug": "electronics"
    }')

echo "Category Response: $CATEGORY_RESPONSE"

# Test Product Creation
echo "Testing product creation..."
PRODUCT_RESPONSE=$(curl -s -X POST "$BASE_URL/products" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $USER_TOKEN" \
    -d '{
        "name": "Smartphone",
        "description": "Latest smartphone with advanced features",
        "sku": "PHONE-001",
        "basePrice": 599.99,
        "currency": "USD",
        "isActive": true,
        "isFeatured": true
    }')

echo "Product Response: $PRODUCT_RESPONSE"

# Test Product List Retrieval
echo "Testing product list retrieval..."
PRODUCTS_LIST_RESPONSE=$(curl -s -X GET "$BASE_URL/products")
echo "Products List Response: $PRODUCTS_LIST_RESPONSE"

# Test Category List Retrieval
echo "Testing category list retrieval..."
CATEGORIES_LIST_RESPONSE=$(curl -s -X GET "$BASE_URL/products/categories")
echo "Categories List Response: $CATEGORIES_LIST_RESPONSE"

echo -e "${YELLOW}Product Catalog Module Test Completed${NC}"
