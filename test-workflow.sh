#!/bin/bash

# E-commerce API Test Workflow
BASE_URL="http://localhost:8080"
RANDOM_ID=$RANDOM

echo "=== E-commerce API Test Workflow ==="
echo "Base URL: $BASE_URL"
echo "Random ID: $RANDOM_ID"
echo

# Test 1: Health Check
echo "1. Testing Health Check..."
curl -s "$BASE_URL/api/health" | jq '.'
echo

# Test 2: User Registration
echo "2. Testing User Registration..."
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d "{
    \"firstName\": \"John\",
    \"lastName\": \"Doe\", 
    \"email\": \"john.doe.$RANDOM_ID@example.com\",
    \"password\": \"Password123!\",
    \"phone\": \"+1234567890\"
  }")

echo "$REGISTER_RESPONSE" | jq '.'

# Extract email for login
EMAIL="john.doe.$RANDOM_ID@example.com"
PASSWORD="Password123!"
echo "Registered email: $EMAIL"
echo

# Test 3: User Login
echo "3. Testing User Login..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d "{
    \"email\": \"$EMAIL\",
    \"password\": \"$PASSWORD\"
  }")

echo "$LOGIN_RESPONSE" | jq '.'

# Extract JWT token
TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.accessToken // empty')
if [ -z "$TOKEN" ]; then
  echo "ERROR: Failed to get JWT token"
  exit 1
fi

echo "JWT Token: ${TOKEN:0:50}..."
echo

# Test 4: Get Products
echo "4. Testing Get Products..."
PRODUCTS_RESPONSE=$(curl -s "$BASE_URL/api/products")
echo "$PRODUCTS_RESPONSE" | jq '.data.content[0]'

# Extract first product ID
PRODUCT_ID=$(echo "$PRODUCTS_RESPONSE" | jq -r '.data.content[0].id // empty')
echo "First Product ID: $PRODUCT_ID"
echo

# Test 5: Get Product Details
echo "5. Testing Get Product Details..."
curl -s "$BASE_URL/api/products/$PRODUCT_ID" | jq '.data'
echo

# Test 6: Get Cart
echo "6. Testing Get Cart..."
curl -s -H "Authorization: Bearer $TOKEN" "$BASE_URL/api/cart" | jq '.data'
echo

# Test 7: Add Item to Cart
echo "7. Testing Add Item to Cart..."
ADD_CART_RESPONSE=$(curl -s -X POST "$BASE_URL/api/cart/items" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"productId\": $PRODUCT_ID,
    \"quantity\": 2
  }")

echo "$ADD_CART_RESPONSE" | jq '.data'

# Extract cart item ID
CART_ITEM_ID=$(echo "$ADD_CART_RESPONSE" | jq -r '.data.items[0].id // empty')
echo "Cart Item ID: $CART_ITEM_ID"
echo

# Test 8: Update Cart Item
echo "8. Testing Update Cart Item..."
curl -s -X PUT "$BASE_URL/api/cart/items/$CART_ITEM_ID" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"quantity\": 3
  }" | jq '.data'
echo

# Test 9: Create Order
echo "9. Testing Create Order..."
ORDER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/orders" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"shippingAddress\": {
      \"street\": \"123 Main St\",
      \"city\": \"New York\",
      \"state\": \"NY\",
      \"zipCode\": \"10001\",
      \"country\": \"US\"
    },
    \"billingAddress\": {
      \"street\": \"123 Main St\",
      \"city\": \"New York\",
      \"state\": \"NY\",
      \"zipCode\": \"10001\",
      \"country\": \"US\"
    }
  }")

echo "$ORDER_RESPONSE" | jq '.data'

# Extract order ID
ORDER_ID=$(echo "$ORDER_RESPONSE" | jq -r '.data.id // empty')
echo "Order ID: $ORDER_ID"
echo

# Test 10: Process Payment
echo "10. Testing Process Payment..."
if [ ! -z "$ORDER_ID" ]; then
  curl -s -X POST "$BASE_URL/api/payments/process" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d "{
      \"orderId\": $ORDER_ID,
      \"amount\": 29.99,
      \"currency\": \"USD\",
      \"paymentMethod\": \"card\",
      \"paymentMethodId\": \"pm_1234567890\",
      \"customerId\": \"cus_1234567890\"
    }" | jq '.data'
else
  echo "Skipping payment test - no order ID"
fi
echo

echo "=== Test Workflow Complete ==="