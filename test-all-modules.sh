#!/bin/bash

# Comprehensive E-commerce Backend Module Testing Script
# Tests all modules: User Management, Product Catalog, Shopping Cart, Order Processing,
# Payment Integration, Search & Recommendations, Notifications, Analytics

BASE_URL="http://localhost:8080/api"
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "🚀 Starting E-commerce Backend Module Testing..."

# Wait for application to be ready
echo "⏳ Waiting for application to start..."
for i in {1..30}; do
    if curl -s -f "$BASE_URL/../health" > /dev/null 2>&1; then
        echo -e "${GREEN}✓ Application is ready!${NC}"
        break
    fi
    if [ $i -eq 30 ]; then
        echo -e "${RED}❌ Application failed to start within 30 seconds${NC}"
        exit 1
    fi
    sleep 1
done

# Global variables for test data
USER_TOKEN=""
USER_ID=""
PRODUCT_ID=""
CART_ID=""
ORDER_ID=""

# Helper function to make HTTP requests
make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    local headers=$4

    if [ -n "$headers" ]; then
        curl -s -X "$method" "$BASE_URL$endpoint" \
             -H "Content-Type: application/json" \
             -H "$headers" \
             -d "$data"
    else
        curl -s -X "$method" "$BASE_URL$endpoint" \
             -H "Content-Type: application/json" \
             -d "$data"
    fi
}

# Test result checker
check_result() {
    local response=$1
    local expected_status=$2
    local test_name=$3

    if echo "$response" | grep -q "\"success\":true" || echo "$response" | grep -q "\"status\":$expected_status"; then
        echo -e "${GREEN}✓ $test_name - PASSED${NC}"
        return 0
    else
        echo -e "${RED}❌ $test_name - FAILED${NC}"
        echo "Response: $response"
        return 1
    fi
}

echo -e "\n${YELLOW}=== MODULE 1: USER MANAGEMENT ===${NC}"

# Test 1.1: User Registration
echo "Testing user registration..."
REGISTER_RESPONSE=$(make_request "POST" "/auth/register" '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "Password123!"
}')

if check_result "$REGISTER_RESPONSE" "201" "User Registration"; then
    USER_ID=$(echo "$REGISTER_RESPONSE" | grep -o '"userId":[0-9]*' | cut -d':' -f2)
fi

# Test 1.2: User Login
echo "Testing user login..."
LOGIN_RESPONSE=$(make_request "POST" "/auth/login" '{
    "email": "john.doe@example.com",
    "password": "Password123!"
}')

if check_result "$LOGIN_RESPONSE" "200" "User Login"; then
    USER_TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)
fi

# Test 1.3: Profile Management
if [ -n "$USER_TOKEN" ]; then
    echo "Testing profile retrieval..."
    PROFILE_RESPONSE=$(make_request "GET" "/users/profile" "" "Authorization: Bearer $USER_TOKEN")
    check_result "$PROFILE_RESPONSE" "200" "Profile Retrieval"
fi

echo -e "\n${YELLOW}=== MODULE 2: PRODUCT CATALOG ===${NC}"

# Test 2.1: Create Category
echo "Testing category creation..."
CATEGORY_RESPONSE=$(make_request "POST" "/products/categories" '{
    "name": "Electronics",
    "slug": "electronics"
}' "Authorization: Bearer $USER_TOKEN")

check_result "$CATEGORY_RESPONSE" "201" "Category Creation"

# Test 2.2: Create Product
echo "Testing product creation..."
PRODUCT_RESPONSE=$(make_request "POST" "/products" '{
    "name": "Smartphone",
    "description": "Latest smartphone with advanced features",
    "sku": "PHONE-001",
    "basePrice": 599.99,
    "currency": "USD",
    "isActive": true,
    "isFeatured": true
}' "Authorization: Bearer $USER_TOKEN")

if check_result "$PRODUCT_RESPONSE" "201" "Product Creation"; then
    PRODUCT_ID=$(echo "$PRODUCT_RESPONSE" | grep -o '"productId":[0-9]*' | cut -d':' -f2)
fi

# Test 2.3: Get Products
echo "Testing product retrieval..."
PRODUCTS_LIST_RESPONSE=$(make_request "GET" "/products" "")
check_result "$PRODUCTS_LIST_RESPONSE" "200" "Product List Retrieval"

echo -e "\n${YELLOW}=== MODULE 3: SHOPPING CART ===${NC}"

if [ -n "$USER_TOKEN" ] && [ -n "$PRODUCT_ID" ]; then
    # Test 3.1: Add to Cart
    echo "Testing add to cart..."
    ADD_CART_RESPONSE=$(make_request "POST" "/cart/add" '{
        "productId": '"$PRODUCT_ID"',
        "quantity": 2
    }' "Authorization: Bearer $USER_TOKEN")

    check_result "$ADD_CART_RESPONSE" "200" "Add to Cart"

    # Test 3.2: Get Cart
    echo "Testing cart retrieval..."
    CART_RESPONSE=$(make_request "GET" "/cart" "" "Authorization: Bearer $USER_TOKEN")
    check_result "$CART_RESPONSE" "200" "Cart Retrieval"

    # Test 3.3: Update Cart Item
    echo "Testing cart item update..."
    UPDATE_CART_RESPONSE=$(make_request "PUT" "/cart/update" '{
        "productId": '"$PRODUCT_ID"',
        "quantity": 3
    }' "Authorization: Bearer $USER_TOKEN")

    check_result "$UPDATE_CART_RESPONSE" "200" "Cart Item Update"
fi

echo -e "\n${YELLOW}=== MODULE 4: ORDER PROCESSING ===${NC}"

if [ -n "$USER_TOKEN" ]; then
    # Test 4.1: Create Order
    echo "Testing order creation..."
    ORDER_RESPONSE=$(make_request "POST" "/orders" '{
        "items": [
            {
                "productId": '"$PRODUCT_ID"',
                "quantity": 2,
                "unitPrice": 599.99
            }
        ]
    }' "Authorization: Bearer $USER_TOKEN")

    if check_result "$ORDER_RESPONSE" "201" "Order Creation"; then
        ORDER_ID=$(echo "$ORDER_RESPONSE" | grep -o '"orderId":[0-9]*' | cut -d':' -f2)
    fi

    # Test 4.2: Get Orders
    echo "Testing order retrieval..."
    ORDERS_RESPONSE=$(make_request "GET" "/orders" "" "Authorization: Bearer $USER_TOKEN")
    check_result "$ORDERS_RESPONSE" "200" "Order List Retrieval"
fi

echo -e "\n${YELLOW}=== MODULE 5: PAYMENT INTEGRATION (MOCKED) ===${NC}"

if [ -n "$USER_TOKEN" ] && [ -n "$ORDER_ID" ]; then
    # Test 5.1: Create Payment Intent
    echo "Testing payment intent creation..."
    PAYMENT_RESPONSE=$(make_request "POST" "/payments/create-intent" '{
        "orderId": '"$ORDER_ID"',
        "amount": 1199.98,
        "currency": "USD"
    }' "Authorization: Bearer $USER_TOKEN")

    check_result "$PAYMENT_RESPONSE" "200" "Payment Intent Creation"
fi

echo -e "\n${YELLOW}=== MODULE 6: SEARCH & RECOMMENDATIONS (MOCKED) ===${NC}"

# Test 6.1: Product Search
echo "Testing product search..."
SEARCH_RESPONSE=$(make_request "GET" "/search?query=smartphone" "")
check_result "$SEARCH_RESPONSE" "200" "Product Search"

# Test 6.2: Get Recommendations
if [ -n "$USER_TOKEN" ]; then
    echo "Testing recommendations..."
    RECOMMENDATIONS_RESPONSE=$(make_request "GET" "/recommendations" "" "Authorization: Bearer $USER_TOKEN")
    check_result "$RECOMMENDATIONS_RESPONSE" "200" "Product Recommendations"
fi

echo -e "\n${YELLOW}=== MODULE 7: NOTIFICATIONS ===${NC}"

if [ -n "$USER_TOKEN" ]; then
    # Test 7.1: Send Notification
    echo "Testing notification sending..."
    NOTIFICATION_RESPONSE=$(make_request "POST" "/notifications/send" '{
        "userId": '"$USER_ID"',
        "type": "ORDER_CONFIRMATION",
        "title": "Order Confirmed",
        "message": "Your order has been confirmed and is being processed."
    }' "Authorization: Bearer $USER_TOKEN")

    check_result "$NOTIFICATION_RESPONSE" "200" "Notification Sending"

    # Test 7.2: Get Notifications
    echo "Testing notification retrieval..."
    NOTIFICATIONS_RESPONSE=$(make_request "GET" "/notifications" "" "Authorization: Bearer $USER_TOKEN")
    check_result "$NOTIFICATIONS_RESPONSE" "200" "Notification Retrieval"
fi

echo -e "\n${YELLOW}=== MODULE 8: ANALYTICS ===${NC}"

if [ -n "$USER_TOKEN" ]; then
    # Test 8.1: Track Event
    echo "Testing event tracking..."
    ANALYTICS_RESPONSE=$(make_request "POST" "/analytics/track" '{
        "eventType": "PRODUCT_VIEW",
        "userId": '"$USER_ID"',
        "details": {
            "productId": "'"$PRODUCT_ID"'",
            "category": "Electronics"
        }
    }' "Authorization: Bearer $USER_TOKEN")

    check_result "$ANALYTICS_RESPONSE" "200" "Event Tracking"

    # Test 8.2: Get Analytics Dashboard
    echo "Testing analytics dashboard..."
    DASHBOARD_RESPONSE=$(make_request "GET" "/analytics/dashboard" "" "Authorization: Bearer $USER_TOKEN")
    check_result "$DASHBOARD_RESPONSE" "200" "Analytics Dashboard"
fi

echo -e "\n${GREEN}🎉 Module testing completed!${NC}"
echo -e "${YELLOW}📋 Summary: All critical e-commerce backend modules have been tested${NC}"
echo -e "${YELLOW}Note: Stripe and Elasticsearch functionalities are mocked as specified${NC}"
