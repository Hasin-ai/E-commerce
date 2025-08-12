#!/bin/bash

# Comprehensive Cart Operations Test Script
# This script tests all cart CRUD operations to ensure seamless user experience

BASE_URL="http://localhost:8080"
CONTENT_TYPE="Content-Type: application/json"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== E-Commerce Cart Operations Test ===${NC}"
echo ""

# Function to print test results
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ $2${NC}"
    else
        echo -e "${RED}✗ $2${NC}"
    fi
}

# Function to authenticate and get token
authenticate() {
    echo -e "${YELLOW}Step 1: Authenticating user...${NC}"

    # Login request
    LOGIN_RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/auth/login" \
        -H "$CONTENT_TYPE" \
        -d '{
            "email": "customer@example.com",
            "password": "password123"
        }')

    HTTP_CODE=$(echo "$LOGIN_RESPONSE" | tail -n1)
    RESPONSE_BODY=$(echo "$LOGIN_RESPONSE" | head -n -1)

    if [ "$HTTP_CODE" -eq 200 ]; then
        TOKEN=$(echo "$RESPONSE_BODY" | jq -r '.data.accessToken')
        print_result 0 "User authenticated successfully"
        echo "Token: ${TOKEN:0:20}..."
    else
        print_result 1 "Authentication failed"
        echo "Response: $RESPONSE_BODY"
        exit 1
    fi
    echo ""
}

# Function to get cart
get_cart() {
    echo -e "${YELLOW}Step 2: Getting current cart...${NC}"

    CART_RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/api/cart" \
        -H "$CONTENT_TYPE" \
        -H "Authorization: Bearer $TOKEN")

    HTTP_CODE=$(echo "$CART_RESPONSE" | tail -n1)
    RESPONSE_BODY=$(echo "$CART_RESPONSE" | head -n -1)

    if [ "$HTTP_CODE" -eq 200 ]; then
        print_result 0 "Cart retrieved successfully"
        echo "Cart details: $(echo "$RESPONSE_BODY" | jq -c '.data')"
    else
        print_result 1 "Failed to get cart"
        echo "Response: $RESPONSE_BODY"
    fi
    echo ""
}

# Function to add item to cart
add_item_to_cart() {
    echo -e "${YELLOW}Step 3: Adding item to cart...${NC}"

    ADD_RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/cart/items" \
        -H "$CONTENT_TYPE" \
        -H "Authorization: Bearer $TOKEN" \
        -d '{
            "productId": 1,
            "quantity": 2
        }')

    HTTP_CODE=$(echo "$ADD_RESPONSE" | tail -n1)
    RESPONSE_BODY=$(echo "$ADD_RESPONSE" | head -n -1)

    if [ "$HTTP_CODE" -eq 201 ]; then
        print_result 0 "Item added to cart successfully"
        CART_ITEM_ID=$(echo "$RESPONSE_BODY" | jq -r '.data.items[0].id')
        echo "Added item ID: $CART_ITEM_ID"
    else
        print_result 1 "Failed to add item to cart"
        echo "Response: $RESPONSE_BODY"
    fi
    echo ""
}

# Function to update cart item
update_cart_item() {
    echo -e "${YELLOW}Step 4: Updating cart item quantity...${NC}"

    UPDATE_RESPONSE=$(curl -s -w "\n%{http_code}" -X PUT "$BASE_URL/api/cart/items/$CART_ITEM_ID" \
        -H "$CONTENT_TYPE" \
        -H "Authorization: Bearer $TOKEN" \
        -d '{
            "quantity": 3
        }')

    HTTP_CODE=$(echo "$UPDATE_RESPONSE" | tail -n1)
    RESPONSE_BODY=$(echo "$UPDATE_RESPONSE" | head -n -1)

    if [ "$HTTP_CODE" -eq 200 ]; then
        print_result 0 "Cart item updated successfully"
        echo "Updated quantity: $(echo "$RESPONSE_BODY" | jq -r '.data.items[0].quantity')"
    else
        print_result 1 "Failed to update cart item"
        echo "Response: $RESPONSE_BODY"
    fi
    echo ""
}

# Function to get cart item count
get_cart_count() {
    echo -e "${YELLOW}Step 5: Getting cart item count...${NC}"

    COUNT_RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/api/cart/count" \
        -H "$CONTENT_TYPE" \
        -H "Authorization: Bearer $TOKEN")

    HTTP_CODE=$(echo "$COUNT_RESPONSE" | tail -n1)
    RESPONSE_BODY=$(echo "$COUNT_RESPONSE" | head -n -1)

    if [ "$HTTP_CODE" -eq 200 ]; then
        print_result 0 "Cart count retrieved successfully"
        echo "Total items: $(echo "$RESPONSE_BODY" | jq -r '.data')"
    else
        print_result 1 "Failed to get cart count"
        echo "Response: $RESPONSE_BODY"
    fi
    echo ""
}

# Function to delete cart item
delete_cart_item() {
    echo -e "${YELLOW}Step 6: Deleting cart item...${NC}"

    DELETE_RESPONSE=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL/api/cart/items/$CART_ITEM_ID" \
        -H "$CONTENT_TYPE" \
        -H "Authorization: Bearer $TOKEN")

    HTTP_CODE=$(echo "$DELETE_RESPONSE" | tail -n1)
    RESPONSE_BODY=$(echo "$DELETE_RESPONSE" | head -n -1)

    if [ "$HTTP_CODE" -eq 200 ]; then
        print_result 0 "Cart item deleted successfully"
        echo "Remaining items: $(echo "$RESPONSE_BODY" | jq -r '.data.items | length')"
    else
        print_result 1 "Failed to delete cart item"
        echo "Response: $RESPONSE_BODY"
    fi
    echo ""
}

# Function to clear entire cart
clear_cart() {
    echo -e "${YELLOW}Step 7: Clearing entire cart...${NC}"

    CLEAR_RESPONSE=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL/api/cart" \
        -H "$CONTENT_TYPE" \
        -H "Authorization: Bearer $TOKEN")

    HTTP_CODE=$(echo "$CLEAR_RESPONSE" | tail -n1)
    RESPONSE_BODY=$(echo "$CLEAR_RESPONSE" | head -n -1)

    if [ "$HTTP_CODE" -eq 200 ]; then
        print_result 0 "Cart cleared successfully"
    else
        print_result 1 "Failed to clear cart"
        echo "Response: $RESPONSE_BODY"
    fi
    echo ""
}

# Function to verify cart is empty
verify_empty_cart() {
    echo -e "${YELLOW}Step 8: Verifying cart is empty...${NC}"

    VERIFY_RESPONSE=$(curl -s -w "\n%{http_code}" -X GET "$BASE_URL/api/cart" \
        -H "$CONTENT_TYPE" \
        -H "Authorization: Bearer $TOKEN")

    HTTP_CODE=$(echo "$VERIFY_RESPONSE" | tail -n1)
    RESPONSE_BODY=$(echo "$VERIFY_RESPONSE" | head -n -1)

    if [ "$HTTP_CODE" -eq 200 ]; then
        ITEM_COUNT=$(echo "$RESPONSE_BODY" | jq -r '.data.totalItems')
        if [ "$ITEM_COUNT" -eq 0 ]; then
            print_result 0 "Cart is empty as expected"
        else
            print_result 1 "Cart is not empty (contains $ITEM_COUNT items)"
        fi
    else
        print_result 1 "Failed to verify empty cart"
        echo "Response: $RESPONSE_BODY"
    fi
    echo ""
}

# Main execution
echo -e "${BLUE}Testing Cart Operations Flow...${NC}"
echo ""

authenticate
get_cart
add_item_to_cart
update_cart_item
get_cart_count
delete_cart_item
clear_cart
verify_empty_cart

echo -e "${BLUE}=== Cart Operations Test Complete ===${NC}"
echo ""
echo -e "${GREEN}Summary:${NC}"
echo "✓ GET /api/cart - Retrieve cart"
echo "✓ POST /api/cart/items - Add item to cart"
echo "✓ PUT /api/cart/items/{itemId} - Update cart item"
echo "✓ GET /api/cart/count - Get cart item count"
echo "✓ DELETE /api/cart/items/{itemId} - Delete cart item"
echo "✓ DELETE /api/cart - Clear entire cart"
echo ""
echo -e "${YELLOW}Note: Make sure the server is running on $BASE_URL${NC}"
