#!/bin/bash

# E-Commerce API Testing Script
# Based on QA Testing Prompt - Customer Journey Workflow

set -e

# Configuration
BASE_URL="http://localhost:8080"
OUTPUT_DIR="test-results"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Create output directory
mkdir -p "$OUTPUT_DIR"

# Log file
LOG_FILE="$OUTPUT_DIR/api_test_$TIMESTAMP.log"

# Helper functions
log() {
    echo -e "$1" | tee -a "$LOG_FILE"
}

test_endpoint() {
    local method="$1"
    local endpoint="$2"
    local expected_status="$3"
    local auth_header="$4"
    local data="$5"
    local description="$6"

    log "${BLUE}Testing: $description${NC}"
    log "  $method $endpoint"

    # Build curl command
    local curl_cmd="curl -s -w 'HTTP_STATUS:%{http_code}' -X $method"

    if [ ! -z "$auth_header" ]; then
        curl_cmd="$curl_cmd -H 'Authorization: Bearer $auth_header'"
    fi

    if [ ! -z "$data" ]; then
        curl_cmd="$curl_cmd -H 'Content-Type: application/json' -d '$data'"
    fi

    curl_cmd="$curl_cmd '$BASE_URL$endpoint'"

    # Execute request
    local response=$(eval $curl_cmd)
    local http_status=$(echo "$response" | grep -o 'HTTP_STATUS:[0-9]*' | cut -d: -f2)
    local body=$(echo "$response" | sed 's/HTTP_STATUS:[0-9]*$//')

    # Check status
    if [ "$http_status" = "$expected_status" ]; then
        log "  ${GREEN}✅ PASSED${NC} (Status: $http_status)"
        echo "$body"
        return 0
    else
        log "  ${RED}❌ FAILED${NC} (Expected: $expected_status, Got: $http_status)"
        log "  Response: $body"
        return 1
    fi
}

check_api_health() {
    log "${YELLOW}🔍 Checking API Health...${NC}"
    if test_endpoint "GET" "/api/health" "200" "" "" "Health Check"; then
        log "${GREEN}API is running and healthy${NC}\n"
        return 0
    else
        log "${RED}API is not responding. Please ensure the server is running on $BASE_URL${NC}"
        exit 1
    fi
}

test_user_registration() {
    log "${YELLOW}🔐 Testing User Registration & Authentication...${NC}"

    # Generate unique email
    local email="testuser_$(date +%s)@example.com"
    local password="SecurePassword123!"

    # Test user registration
    local registration_data='{
        "firstName": "Test",
        "lastName": "User",
        "email": "'$email'",
        "password": "'$password'",
        "phone": "+1234567890"
    }'

    local reg_response=$(test_endpoint "POST" "/api/auth/register" "201" "" "$registration_data" "User Registration")

    if [ $? -eq 0 ]; then
        # Extract user ID
        USER_ID=$(echo "$reg_response" | jq -r '.data.id' 2>/dev/null || echo "")
        log "  User ID: $USER_ID"

        # Test user login
        local login_data='{
            "email": "'$email'",
            "password": "'$password'"
        }'

        local login_response=$(test_endpoint "POST" "/api/auth/login" "200" "" "$login_data" "User Login")

        if [ $? -eq 0 ]; then
            # Extract auth token
            AUTH_TOKEN=$(echo "$login_response" | jq -r '.data.accessToken' 2>/dev/null || echo "")
            log "  ${GREEN}Authentication successful${NC}"
            log "  Token: ${AUTH_TOKEN:0:20}..."
            return 0
        fi
    fi

    return 1
}

test_product_browsing() {
    log "${YELLOW}🛍️ Testing Product Browsing...${NC}"

    # Test get all products
    local products_response=$(test_endpoint "GET" "/api/products?page=0&size=10" "200" "" "" "Get All Products")

    if [ $? -eq 0 ]; then
        # Extract first product ID
        PRODUCT_ID=$(echo "$products_response" | jq -r '.data.content[0].id' 2>/dev/null || echo "")

        if [ ! -z "$PRODUCT_ID" ] && [ "$PRODUCT_ID" != "null" ]; then
            log "  Product ID for testing: $PRODUCT_ID"

            # Test get product by ID
            test_endpoint "GET" "/api/products/$PRODUCT_ID" "200" "" "" "Get Product Details"

            # Test category filter
            test_endpoint "GET" "/api/products?category=electronics" "200" "" "" "Filter by Category"
        else
            log "  ${YELLOW}⚠️ No products found for detailed testing${NC}"
        fi

        return 0
    fi

    return 1
}

test_cart_operations() {
    log "${YELLOW}🛒 Testing Cart Operations...${NC}"

    if [ -z "$AUTH_TOKEN" ]; then
        log "  ${RED}❌ No auth token available${NC}"
        return 1
    fi

    # Test get cart
    test_endpoint "GET" "/api/cart" "200" "$AUTH_TOKEN" "" "Get Current Cart"

    if [ $? -eq 0 ] && [ ! -z "$PRODUCT_ID" ] && [ "$PRODUCT_ID" != "null" ]; then
        # Test add item to cart
        local add_item_data='{
            "productId": "'$PRODUCT_ID'",
            "quantity": 2
        }'

        local cart_response=$(test_endpoint "POST" "/api/cart/items" "201" "$AUTH_TOKEN" "$add_item_data" "Add Item to Cart")

        if [ $? -eq 0 ]; then
            # Extract cart item ID
            CART_ITEM_ID=$(echo "$cart_response" | jq -r '.data.items[0].id' 2>/dev/null || echo "")

            if [ ! -z "$CART_ITEM_ID" ] && [ "$CART_ITEM_ID" != "null" ]; then
                # Test update cart item
                local update_data='{"quantity": 3}'
                test_endpoint "PUT" "/api/cart/items/$CART_ITEM_ID" "200" "$AUTH_TOKEN" "$update_data" "Update Cart Item"
            fi
        fi

        return 0
    fi

    return 1
}

test_order_placement() {
    log "${YELLOW}📦 Testing Order Placement...${NC}"

    if [ -z "$AUTH_TOKEN" ]; then
        log "  ${RED}❌ No auth token available${NC}"
        return 1
    fi

    # Test place order
    local order_data='{
        "shippingAddress": {
            "street": "123 Test St",
            "city": "Test City",
            "state": "TS",
            "zipCode": "12345",
            "country": "US"
        }
    }'

    local order_response=$(test_endpoint "POST" "/api/orders" "201" "$AUTH_TOKEN" "$order_data" "Place Order")

    if [ $? -eq 0 ]; then
        # Extract order ID
        ORDER_ID=$(echo "$order_response" | jq -r '.data.id' 2>/dev/null || echo "")
        log "  Order ID: $ORDER_ID"

        # Test get order history
        test_endpoint "GET" "/api/orders" "200" "$AUTH_TOKEN" "" "Get Order History"

        # Test get order details
        if [ ! -z "$ORDER_ID" ] && [ "$ORDER_ID" != "null" ]; then
            test_endpoint "GET" "/api/orders/$ORDER_ID" "200" "$AUTH_TOKEN" "" "Get Order Details"
        fi

        return 0
    fi

    return 1
}

test_payment_processing() {
    log "${YELLOW}💳 Testing Payment Processing...${NC}"

    if [ -z "$AUTH_TOKEN" ] || [ -z "$ORDER_ID" ]; then
        log "  ${RED}❌ No auth token or order ID available${NC}"
        return 1
    fi

    # Test payment processing
    local payment_data='{
        "orderId": "'$ORDER_ID'",
        "amount": 29.99,
        "currency": "USD",
        "paymentMethod": "card",
        "cardDetails": {
            "cardNumber": "4242424242424242",
            "expiryMonth": "12",
            "expiryYear": "2025",
            "cvv": "123"
        }
    }'

    local payment_response=$(test_endpoint "POST" "/api/payments" "201" "$AUTH_TOKEN" "$payment_data" "Process Payment")

    if [ $? -eq 0 ]; then
        # Extract payment ID
        PAYMENT_ID=$(echo "$payment_response" | jq -r '.data.id' 2>/dev/null || echo "")
        log "  Payment ID: $PAYMENT_ID"

        # Test get payment details
        if [ ! -z "$PAYMENT_ID" ] && [ "$PAYMENT_ID" != "null" ]; then
            test_endpoint "GET" "/api/payments/$PAYMENT_ID" "200" "$AUTH_TOKEN" "" "Get Payment Details"
        fi

        return 0
    fi

    return 1
}

test_notifications() {
    log "${YELLOW}🔔 Testing Notifications...${NC}"

    if [ -z "$AUTH_TOKEN" ]; then
        log "  ${RED}❌ No auth token available${NC}"
        return 1
    fi

    # Test get notifications
    test_endpoint "GET" "/api/notifications" "200" "$AUTH_TOKEN" "" "Get User Notifications"

    return $?
}

test_error_scenarios() {
    log "${YELLOW}⚠️ Testing Error Scenarios...${NC}"

    # Test unauthorized access
    test_endpoint "GET" "/api/cart" "401" "" "" "Unauthorized Cart Access"

    # Test invalid product ID
    test_endpoint "GET" "/api/products/999999" "404" "" "" "Invalid Product ID"

    # Test invalid login
    local invalid_login='{
        "email": "invalid@example.com",
        "password": "wrongpassword"
    }'

    test_endpoint "POST" "/api/auth/login" "401" "" "$invalid_login" "Invalid Credentials"

    return 0
}

# Main execution
main() {
    log "${BLUE}🚀 E-Commerce API Test Suite${NC}"
    log "========================================"
    log "Timestamp: $(date)"
    log "Base URL: $BASE_URL"
    log "Log file: $LOG_FILE"
    log ""

    # Check if jq is available
    if ! command -v jq &> /dev/null; then
        log "${YELLOW}⚠️ jq not found. JSON parsing will be limited.${NC}"
    fi

    # Initialize counters
    local total_tests=0
    local passed_tests=0

    # Run test suites
    local test_suites=(
        "check_api_health"
        "test_user_registration"
        "test_product_browsing"
        "test_cart_operations"
        "test_order_placement"
        "test_payment_processing"
        "test_notifications"
        "test_error_scenarios"
    )

    for test_suite in "${test_suites[@]}"; do
        total_tests=$((total_tests + 1))
        log ""

        if $test_suite; then
            passed_tests=$((passed_tests + 1))
            log "${GREEN}✅ $test_suite PASSED${NC}"
        else
            log "${RED}❌ $test_suite FAILED${NC}"
        fi
    done

    # Print summary
    log ""
    log "========================================"
    log "${BLUE}📊 TEST SUMMARY${NC}"
    log "========================================"
    log "Total Tests: $total_tests"
    log "Passed: $passed_tests"
    log "Failed: $((total_tests - passed_tests))"
    log ""

    if [ $passed_tests -eq $total_tests ]; then
        log "${GREEN}🎉 All tests passed! E-commerce API is functioning correctly.${NC}"
        exit 0
    else
        log "${RED}⚠️ Some tests failed. Check the logs above for details.${NC}"
        exit 1
    fi
}

# Check for help flag
if [[ "$1" == "-h" || "$1" == "--help" ]]; then
    echo "E-Commerce API Testing Script"
    echo ""
    echo "Usage: $0 [BASE_URL]"
    echo ""
    echo "Arguments:"
    echo "  BASE_URL    Base URL of the API (default: http://localhost:8080)"
    echo ""
    echo "Options:"
    echo "  -h, --help  Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0                          # Test against localhost:8080"
    echo "  $0 http://api.example.com   # Test against custom URL"
    exit 0
fi

# Override base URL if provided
if [ ! -z "$1" ]; then
    BASE_URL="$1"
fi

# Run main function
main
