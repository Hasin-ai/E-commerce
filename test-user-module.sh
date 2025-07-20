#!/bin/bash

# User Management Module Testing Script
BASE_URL="http://localhost:8080/api"
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}=== TESTING USER MANAGEMENT MODULE ===${NC}"

# Test User Registration
echo "Testing user registration..."
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/register" \
    -H "Content-Type: application/json" \
    -d '{
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "password": "Password123!"
    }')

echo "Registration Response: $REGISTER_RESPONSE"

# Test User Login
echo "Testing user login..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{
        "email": "john.doe@example.com",
        "password": "Password123!"
    }')

echo "Login Response: $LOGIN_RESPONSE"

# Extract token if available
USER_TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)

if [ -n "$USER_TOKEN" ]; then
    echo -e "${GREEN}✓ JWT Token obtained: ${USER_TOKEN:0:20}...${NC}"

    # Test Profile Retrieval
    echo "Testing profile retrieval..."
    PROFILE_RESPONSE=$(curl -s -X GET "$BASE_URL/users/profile" \
        -H "Authorization: Bearer $USER_TOKEN")

    echo "Profile Response: $PROFILE_RESPONSE"
else
    echo -e "${RED}❌ Failed to obtain JWT token${NC}"
fi

echo -e "${YELLOW}User Management Module Test Completed${NC}"
