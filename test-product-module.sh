#!/bin/bash

# This script tests the Product Catalog module of the E-commerce API.
# It assumes that the application is running on http://localhost:8080
# and that a user is already registered with the email "john.doe@example.com"
# and password "SecurePass123!".

# Function to log in and get a JWT token
get_token() {
  local response=$(curl -s -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{
      "email": "john.doe@example.com",
      "password": "SecurePass123!"
    }')
  
  local token=$(echo $response | jq -r '.data.accessToken')
  
  if [ -z "$token" ] || [ "$token" == "null" ]; then
    echo "Error: Could not get JWT token. Please make sure the user is registered and the credentials are correct."
    exit 1
  fi
  
  echo $token
}

# Get JWT token
TOKEN=$(get_token)
echo "JWT Token: $TOKEN"
echo

# Create a new product
echo "Creating a new product..."
create_response=$(curl -s -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Test Product",
    "slug": "test-product",
    "description": "This is a test product.",
    "sku": "TEST-SKU-123",
    "basePrice": 99.99,
    "currency": "USD"
  }')

echo "Create Response: $create_response"
product_id=$(echo $create_response | jq -r '.data.id')
echo "Product ID: $product_id"
echo

# Get the product by ID
echo "Getting the product by ID..."
get_response=$(curl -s -X GET http://localhost:8080/api/products/$product_id \
  -H "Authorization: Bearer $TOKEN")

echo "Get Response: $get_response"
echo

# Get all products
echo "Getting all products..."
get_all_response=$(curl -s -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer $TOKEN")

echo "Get All Response: $get_all_response"
echo

# Update the product
echo "Updating the product..."
update_response=$(curl -s -X PUT http://localhost:8080/api/products/$product_id \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Updated Test Product",
    "basePrice": 109.99
  }')

echo "Update Response: $update_response"
echo

# Get the product by ID again to see the changes
echo "Getting the product by ID again..."
get_updated_response=$(curl -s -X GET http://localhost:8080/api/products/$product_id \
  -H "Authorization: Bearer $TOKEN")

echo "Get Updated Response: $get_updated_response"
echo

# Delete the product
echo "Deleting the product..."
delete_response=$(curl -s -X DELETE http://localhost:8080/api/products/$product_id \
  -H "Authorization: Bearer $TOKEN")

echo "Delete Response: $delete_response"
echo

# Get the product by ID again to confirm it's deleted
echo "Getting the product by ID again to confirm deletion..."
get_deleted_response=$(curl -s -X GET http://localhost:8080/api/products/$product_id \
  -H "Authorization: Bearer $TOKEN")

echo "Get Deleted Response: $get_deleted_response"
echo
