#!/bin/bash

# Comprehensive E-commerce API Test Script
# This script tests the complete e-commerce workflow

BASE_URL="http://localhost:8080"
TEST_EMAIL="test.user.$(date +%s)@example.com"
TEST_PASSWORD="SecurePassword123!"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counters
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Function to print test results
print_test_result() {
    local test_name="$1"
    local status="$2"
    local details="$3"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    if [ "$status" = "PASS" ]; then
        echo -e "${GREEN}✓ PASS${NC}: $test_name"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "${RED}✗ FAIL${NC}: $test_name"
        if [ -n "$details" ]; then
            echo -e "  ${YELLOW}Details:${NC} $details"
        fi
        FAILED_TESTS=$((FAILED_TESTS + 1))
    fi
}

# Function to make HTTP requests and check status
make_request() {
    local method="$1"
    local url="$2"
    local data="$3"
    local headers="$4"
    local expected_status="$5"
    
    if [ -n "$data" ]; then
        if [ -n "$headers" ]; then
            response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X "$method" "$url" -H "Content-Type: application/json" -H "$headers" -d "$data")
        else
            response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X "$method" "$url" -H "Content-Type: application/json" -d "$data")
        fi
    else
        if [ -n "$headers" ]; then
            response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X "$method" "$url" -H "$headers")
        else
            response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X "$method" "$url")
        fi
    fi
    
    http_code=$(echo "$response" | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
    body=$(echo "$response" | sed -e 's/HTTPSTATUS:.*//g')
    
    if [ "$http_code" -eq "$expected_status" ]; then
        echo "$body"
        return 0
    else
        echo "Expected status $expected_status but got $http_code: $body" >&2
        return 1
    fi
}

echo "🚀 Starting Comprehensive E-commerce API Tests"
echo "================================================"

# 1. Health Check Tests
echo -e "\n${YELLOW}1. Health Check Tests${NC}"
echo "------------------------"

# Basic health check
if result=$(make_request "GET" "$BASE_URL/api/health" "" "" 200); then
    if echo "$result" | jq -e '.status == "UP"' > /dev/null 2>&1; then
        print_test_result "Basic Health Check" "PASS"
    else
        print_test_result "Basic Health Check" "FAIL" "Status not UP"
    fi
else
    print_test_result "Basic Health Check" "FAIL" "Request failed"
fi

# Detailed health check
if result=$(make_request "GET" "$BASE_URL/api/health/detailed" "" "" 200); then
    if echo "$result" | jq -e '.data.application and .data.database and .data.system' > /dev/null 2>&1; then
        print_test_result "Detailed Health Check" "PASS"
    else
        print_test_result "Detailed Health Check" "FAIL" "Missing health data"
    fi
else
    print_test_result "Detailed Health Check" "FAIL" "Request failed"
fi

# 2. Authentication Tests
echo -e "\n${YELLOW}2. Authentication Tests${NC}"
echo "----------------------------"

# User Registration
register_data="{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"$TEST_EMAIL\",\"password\":\"$TEST_PASSWORD\",\"phone\":\"+1234567890\"}"
if result=$(make_request "POST" "$BASE_URL/api/auth/register" "$register_data" "" 201); then
    if user_id=$(echo "$result" | jq -r '.data.id'); then
        print_test_result "User Registration" "PASS"
    else
        print_test_result "User Registration" "FAIL" "No user ID returned"
    fi
else
    print_test_result "User Registration" "FAIL" "Registration request failed"
fi

# User Login
login_data="{\"email\":\"$TEST_EMAIL\",\"password\":\"$TEST_PASSWORD\"}"
if result=$(make_request "POST" "$BASE_URL/api/auth/login" "$login_data" "" 200); then
    if auth_token=$(echo "$result" | jq -r '.data.accessToken'); then
        print_test_result "User Login" "PASS"
        AUTH_HEADER="Authorization: Bearer $auth_token"
    else
        print_test_result "User Login" "FAIL" "No access token returned"
        AUTH_HEADER=""
    fi
else
    print_test_result "User Login" "FAIL" "Login request failed"
    AUTH_HEADER=""
fi

# 3. Product Tests
echo -e "\n${YELLOW}3. Product Tests${NC}"
echo "--------------------"

# List Products
if result=$(make_request "GET" "$BASE_URL/api/products?page=0&size=10" "" "" 200); then
    if product_id=$(echo "$result" | jq -r '.data.content[0].id'); then
        print_test_result "List Products" "PASS"
    else
        print_test_result "List Products" "FAIL" "No products found"
        product_id="1"  # fallback
    fi
else
    print_test_result "List Products" "FAIL" "Request failed"
    product_id="1"  # fallback
fi

# Get Product Details
if result=$(make_request "GET" "$BASE_URL/api/products/$product_id" "" "" 200); then
    if echo "$result" | jq -e '.data.id and .data.name and .data.price' > /dev/null 2>&1; then
        print_test_result "Get Product Details" "PASS"
    else
        print_test_result "Get Product Details" "FAIL" "Missing product fields"
    fi
else
    print_test_result "Get Product Details" "FAIL" "Request failed"
fi

# Get Featured Products
if result=$(make_request "GET" "$BASE_URL/api/products/featured?page=0&size=10" "" "" 200); then
    print_test_result "Get Featured Products" "PASS"
else
    print_test_result "Get Featured Products" "FAIL" "Request failed"
fi

# Get Product by Slug
if result=$(make_request "GET" "$BASE_URL/api/products/slug/gaming-laptop" "" "" 200); then
    print_test_result "Get Product by Slug" "PASS"
else
    print_test_result "Get Product by Slug" "FAIL" "Request failed"
fi

# 4. Cart Tests (Authenticated)
echo -e "\n${YELLOW}4. Cart Tests${NC}"
echo "-----------------"

if [ -n "$AUTH_HEADER" ]; then
    # Get Current Cart
    if result=$(make_request "GET" "$BASE_URL/api/cart" "" "$AUTH_HEADER" 200); then
        print_test_result "Get Current Cart" "PASS"
    else
        print_test_result "Get Current Cart" "FAIL" "Request failed"
    fi
    
    # Add Item to Cart
    add_cart_data="{\"productId\":$product_id,\"quantity\":2}"
    if result=$(make_request "POST" "$BASE_URL/api/cart/items" "$add_cart_data" "$AUTH_HEADER" 201); then
        if cart_item_id=$(echo "$result" | jq -r '.data.items[0].id'); then
            print_test_result "Add Item to Cart" "PASS"
        else
            print_test_result "Add Item to Cart" "FAIL" "No cart item ID returned"
            cart_item_id="1"  # fallback
        fi
    else
        print_test_result "Add Item to Cart" "FAIL" "Request failed"
        cart_item_id="1"  # fallback
    fi
    
    # Update Cart Item Quantity
    update_cart_data="{\"quantity\":3}"
    if result=$(make_request "PUT" "$BASE_URL/api/cart/items/$cart_item_id" "$update_cart_data" "$AUTH_HEADER" 200); then
        print_test_result "Update Cart Item Quantity" "PASS"
    else
        print_test_result "Update Cart Item Quantity" "FAIL" "Request failed"
    fi
    
    # Get Cart Count
    if result=$(make_request "GET" "$BASE_URL/api/cart/count" "" "$AUTH_HEADER" 200); then
        print_test_result "Get Cart Count" "PASS"
    else
        print_test_result "Get Cart Count" "FAIL" "Request failed"
    fi
else
    print_test_result "Get Current Cart" "FAIL" "No authentication token"
    print_test_result "Add Item to Cart" "FAIL" "No authentication token"
    print_test_result "Update Cart Item Quantity" "FAIL" "No authentication token"
    print_test_result "Get Cart Count" "FAIL" "No authentication token"
fi

# 5. Search Tests
echo -e "\n${YELLOW}5. Search Tests${NC}"
echo "-------------------"

# Search Products
if result=$(make_request "GET" "$BASE_URL/api/search/products?query=laptop&page=0&size=10" "" "" 200); then
    print_test_result "Search Products" "PASS"
else
    print_test_result "Search Products" "FAIL" "Request failed"
fi

# Get Recommendations
if result=$(make_request "GET" "$BASE_URL/api/search/recommendations?limit=10" "" "" 200); then
    print_test_result "Get Recommendations" "PASS"
else
    print_test_result "Get Recommendations" "FAIL" "Request failed"
fi

# Get Search Suggestions
if result=$(make_request "GET" "$BASE_URL/api/search/suggestions?query=lap&limit=5" "" "" 200); then
    print_test_result "Get Search Suggestions" "PASS"
else
    print_test_result "Get Search Suggestions" "FAIL" "Request failed"
fi

# 6. Order Tests (Authenticated)
echo -e "\n${YELLOW}6. Order Tests${NC}"
echo "------------------"

if [ -n "$AUTH_HEADER" ]; then
    # Place Order
    order_data="{\"shippingAddress\":{\"street\":\"123 Main St\",\"city\":\"New York\",\"state\":\"NY\",\"zipCode\":\"10001\",\"country\":\"US\"},\"billingAddress\":{\"street\":\"123 Main St\",\"city\":\"New York\",\"state\":\"NY\",\"zipCode\":\"10001\",\"country\":\"US\"}}"
    if result=$(make_request "POST" "$BASE_URL/api/orders" "$order_data" "$AUTH_HEADER" 201); then
        if order_id=$(echo "$result" | jq -r '.data.id'); then
            print_test_result "Place Order" "PASS"
        else
            print_test_result "Place Order" "FAIL" "No order ID returned"
            order_id="1"  # fallback
        fi
    else
        print_test_result "Place Order" "FAIL" "Request failed"
        order_id="1"  # fallback
    fi
    
    # View Order History
    if result=$(make_request "GET" "$BASE_URL/api/orders" "" "$AUTH_HEADER" 200); then
        print_test_result "View Order History" "PASS"
    else
        print_test_result "View Order History" "FAIL" "Request failed"
    fi
    
    # View Order Details
    if result=$(make_request "GET" "$BASE_URL/api/orders/$order_id" "" "$AUTH_HEADER" 200); then
        print_test_result "View Order Details" "PASS"
    else
        print_test_result "View Order Details" "FAIL" "Request failed"
    fi
else
    print_test_result "Place Order" "FAIL" "No authentication token"
    print_test_result "View Order History" "FAIL" "No authentication token"
    print_test_result "View Order Details" "FAIL" "No authentication token"
fi

# 7. Error Scenario Tests
echo -e "\n${YELLOW}7. Error Scenario Tests${NC}"
echo "----------------------------"

# Invalid Product ID
if result=$(make_request "GET" "$BASE_URL/api/products/999999" "" "" 404); then
    print_test_result "Invalid Product ID Returns 404" "PASS"
else
    print_test_result "Invalid Product ID Returns 404" "FAIL" "Expected 404 status"
fi

# Unauthorized Access
if result=$(make_request "GET" "$BASE_URL/api/cart" "" "" 401); then
    print_test_result "Unauthorized Access Returns 401" "PASS"
else
    print_test_result "Unauthorized Access Returns 401" "FAIL" "Expected 401 status"
fi

# Invalid Login Credentials
invalid_login_data="{\"email\":\"invalid@example.com\",\"password\":\"wrongpassword\"}"
if result=$(make_request "POST" "$BASE_URL/api/auth/login" "$invalid_login_data" "" 401); then
    print_test_result "Invalid Login Credentials" "PASS"
else
    print_test_result "Invalid Login Credentials" "FAIL" "Expected 401 status"
fi

# Duplicate Email Registration
if result=$(make_request "POST" "$BASE_URL/api/auth/register" "$register_data" "" 400); then
    print_test_result "Duplicate Email Registration" "PASS"
else
    print_test_result "Duplicate Email Registration" "FAIL" "Expected 400 status"
fi

# 8. Cleanup Tests (if authenticated)
echo -e "\n${YELLOW}8. Cleanup Tests${NC}"
echo "---------------------"

if [ -n "$AUTH_HEADER" ]; then
    # Remove Item from Cart
    if result=$(make_request "DELETE" "$BASE_URL/api/cart/items/$cart_item_id" "" "$AUTH_HEADER" 200); then
        print_test_result "Remove Item from Cart" "PASS"
    else
        print_test_result "Remove Item from Cart" "FAIL" "Request failed"
    fi
    
    # Clear Cart
    if result=$(make_request "DELETE" "$BASE_URL/api/cart" "" "$AUTH_HEADER" 200); then
        print_test_result "Clear Cart" "PASS"
    else
        print_test_result "Clear Cart" "FAIL" "Request failed"
    fi
    
    # Logout
    if result=$(make_request "POST" "$BASE_URL/api/auth/logout" "" "$AUTH_HEADER" 200); then
        print_test_result "User Logout" "PASS"
    else
        print_test_result "User Logout" "FAIL" "Request failed"
    fi
else
    print_test_result "Remove Item from Cart" "FAIL" "No authentication token"
    print_test_result "Clear Cart" "FAIL" "No authentication token"
    print_test_result "User Logout" "FAIL" "No authentication token"
fi

# Final Results
echo -e "\n${YELLOW}Test Results Summary${NC}"
echo "========================"
echo -e "Total Tests: $TOTAL_TESTS"
echo -e "${GREEN}Passed: $PASSED_TESTS${NC}"
echo -e "${RED}Failed: $FAILED_TESTS${NC}"

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "\n${GREEN}🎉 All tests passed! The API is working correctly.${NC}"
    exit 0
else
    echo -e "\n${RED}❌ Some tests failed. Please check the issues above.${NC}"
    exit 1
fi