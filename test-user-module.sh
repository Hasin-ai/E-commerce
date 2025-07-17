#!/bin/bash

# Test script for User Management module
BASE_URL="http://localhost:8080/api"

echo "Testing User Management Module..."
echo "================================="

# Test 1: User Registration
echo "1. Testing User Registration..."
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "SecurePass123!",
    "phone": "+1234567890"
  }')

if [[ $? -eq 0 ]] && echo "$REGISTER_RESPONSE" | grep -q "success"; then
    echo "✓ User registration successful"
    echo "$REGISTER_RESPONSE" | jq .
else
    echo "✗ User registration failed"
    echo "$REGISTER_RESPONSE"
fi

echo ""

# Test 2: User Login
echo "2. Testing User Login..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePass123!"
  }')

if [[ $? -eq 0 ]] && echo "$LOGIN_RESPONSE" | grep -q "accessToken"; then
    echo "✓ User login successful"
    # Extract token for further tests
    TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.accessToken')
    echo "Token: $TOKEN"
else
    echo "✗ User login failed"
    echo "$LOGIN_RESPONSE"
fi

echo ""

# Test 3: Get User Profile (if token available)
if [[ -n "$TOKEN" && "$TOKEN" != "null" ]]; then
    echo "3. Testing Get User Profile..."
    PROFILE_RESPONSE=$(curl -s -X GET "$BASE_URL/users/profile" \
      -H "Authorization: Bearer $TOKEN")

    if [[ $? -eq 0 ]] && echo "$PROFILE_RESPONSE" | grep -q "success"; then
        echo "✓ Get user profile successful"
        echo "$PROFILE_RESPONSE" | jq .
    else
        echo "✗ Get user profile failed"
        echo "$PROFILE_RESPONSE"
    fi
else
    echo "3. Skipping profile test - no valid token"
fi

echo ""
echo "Test completed!"
