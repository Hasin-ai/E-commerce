#!/bin/bash

echo "=== Testing Complete Authentication Flow ==="

# Step 1: Register a user
echo "1. Registering user..."
REGISTER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "User",
    "email": "testflow@example.com",
    "password": "TestPassword123!",
    "phone": "+1234567890"
  }')

echo "Registration Response: $REGISTER_RESPONSE"

# Step 2: Login with the registered user
echo -e "\n2. Logging in..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testflow@example.com",
    "password": "TestPassword123!"
  }')

echo "Login Response: $LOGIN_RESPONSE"

# Extract token from login response
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
echo "Extracted Token: $TOKEN"

# Step 3: Test authenticated endpoint (get cart)
echo -e "\n3. Testing authenticated endpoint (cart)..."
CART_RESPONSE=$(curl -s -X GET http://localhost:8080/api/cart \
  -H "Authorization: Bearer $TOKEN")

echo "Cart Response: $CART_RESPONSE"

# Step 4: Test adding item to cart
echo -e "\n4. Adding item to cart..."
ADD_CART_RESPONSE=$(curl -s -X POST http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "1",
    "quantity": 2
  }')

echo "Add to Cart Response: $ADD_CART_RESPONSE"

echo -e "\n=== Authentication Flow Test Complete ==="