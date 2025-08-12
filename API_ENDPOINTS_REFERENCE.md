# E-Commerce API Reference

## Base URL
```
http://localhost:8080
```

## Authentication
Most endpoints require JWT authentication. Include the token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## Response Format
All API responses follow this standardized format:
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { /* response data */ },
  "timestamp": "2025-08-08T12:30:45"
}
```

---

## 🔐 Authentication Endpoints

### POST /api/auth/register
Register a new user account.

**Request:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "Password123!",
  "phone": "+1234567890"
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890",
    "active": true,
    "emailVerified": false,
    "createdAt": "2025-08-08T12:30:45",
    "updatedAt": "2025-08-08T12:30:45"
  },
  "timestamp": "2025-08-08T12:30:45"
}
```

### POST /api/auth/login
Authenticate user and get access token.

**Request:**
```json
{
  "email": "john.doe@example.com",
  "password": "Password123!"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "phone": "+1234567890",
      "active": true,
      "emailVerified": false,
      "createdAt": "2025-08-08T12:30:45",
      "updatedAt": "2025-08-08T12:30:45"
    }
  },
  "timestamp": "2025-08-08T12:30:45"
}
```

### POST /api/auth/logout
Logout user (client-side token removal).

**Response (200):**
```json
{
  "success": true,
  "message": "Logout successful",
  "data": null,
  "timestamp": "2025-08-08T12:30:45"
}
```

---

## 📦 Product Endpoints

### GET /api/products
Get all products with pagination and filtering.

**Query Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `category` (optional): Category ID filter
- `search` (optional): Search term
- `featured` (optional): Filter featured products (true/false)

**Response (200):**
```json
{
  "success": true,
  "message": "Products retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Smartphone XYZ",
        "description": "Latest smartphone with advanced features",
        "price": 699.99,
        "stockQuantity": 50,
        "category": {
          "id": 1,
          "name": "Electronics"
        },
        "imageUrl": "https://example.com/images/phone.jpg",
        "slug": "smartphone-xyz",
        "featured": true,
        "active": true,
        "createdAt": "2025-08-08T12:30:45",
        "updatedAt": "2025-08-08T12:30:45"
      }
    ],
    "totalElements": 100,
    "totalPages": 5,
    "size": 20,
    "number": 0
  },
  "timestamp": "2025-08-08T12:30:45"
}
```

### GET /api/products/{id}
Get product by ID.

**Response (200):**
```json
{
  "success": true,
  "message": "Product retrieved successfully",
  "data": {
    "id": 1,
    "name": "Smartphone XYZ",
    "description": "Latest smartphone with advanced features",
    "price": 699.99,
    "stockQuantity": 50,
    "category": {
      "id": 1,
      "name": "Electronics"
    },
    "imageUrl": "https://example.com/images/phone.jpg",
    "slug": "smartphone-xyz",
    "featured": true,
    "active": true,
    "createdAt": "2025-08-08T12:30:45",
    "updatedAt": "2025-08-08T12:30:45"
  },
  "timestamp": "2025-08-08T12:30:45"
}
```

### GET /api/products/slug/{slug}
Get product by slug.

**Response (200):** Same as GET /api/products/{id}

### POST /api/products
Create a new product. (Admin only)

**Request:**
```json
{
  "name": "New Product",
  "description": "Product description",
  "price": 99.99,
  "stockQuantity": 100,
  "categoryId": 1,
  "imageUrl": "https://example.com/images/product.jpg",
  "featured": false
}
```

### PUT /api/products/{id}
Update an existing product. (Admin only)

### DELETE /api/products/{id}
Delete a product. (Admin only)

---

## 🛒 Cart Endpoints

### GET /api/cart
Get user's cart. (Authenticated)

**Response (200):**
```json
{
  "success": true,
  "message": "Cart retrieved successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Smartphone XYZ",
        "productPrice": 699.99,
        "quantity": 2,
        "subtotal": 1399.98
      }
    ],
    "totalAmount": 1399.98,
    "itemCount": 2,
    "createdAt": "2025-08-08T12:30:45",
    "updatedAt": "2025-08-08T12:30:45"
  },
  "timestamp": "2025-08-08T12:30:45"
}
```

### POST /api/cart/items
Add item to cart. (Authenticated)

**Request:**
```json
{
  "productId": 1,
  "quantity": 2
}
```

### PUT /api/cart/items/{itemId}
Update cart item quantity. (Authenticated)

**Request:**
```json
{
  "quantity": 3
}
```

### DELETE /api/cart/items/{itemId}
Remove item from cart. (Authenticated)

### DELETE /api/cart
Clear entire cart. (Authenticated)

---

## 📋 Order Endpoints

### POST /api/orders
Create a new order. (Authenticated)

**Request:**
```json
{
  "shippingAddress": {
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  },
  "billingAddress": {
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  },
  "notes": "Please deliver during business hours"
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "Order created successfully",
  "data": {
    "id": 1,
    "orderNumber": "ORD-2025-001",
    "userId": 1,
    "status": "PENDING",
    "totalAmount": 1399.98,
    "shippingAddress": {
      "street": "123 Main St",
      "city": "New York",
      "state": "NY",
      "zipCode": "10001",
      "country": "USA"
    },
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Smartphone XYZ",
        "price": 699.99,
        "quantity": 2,
        "subtotal": 1399.98
      }
    ],
    "createdAt": "2025-08-08T12:30:45",
    "updatedAt": "2025-08-08T12:30:45"
  },
  "timestamp": "2025-08-08T12:30:45"
}
```

### GET /api/orders
Get user's orders with pagination. (Authenticated)

### GET /api/orders/{id}
Get specific order details. (Authenticated)

---

## 💳 Payment Endpoints

### POST /api/payments/process
Process payment for an order. (Authenticated)

**Request:**
```json
{
  "orderId": 1,
  "paymentMethod": "CREDIT_CARD",
  "amount": 1399.98,
  "currency": "USD",
  "paymentDetails": {
    "cardNumber": "4111111111111111",
    "expiryMonth": "12",
    "expiryYear": "2026",
    "cvv": "123",
    "cardHolderName": "John Doe"
  }
}
```

### GET /api/payments/{id}
Get payment details. (Authenticated)

### GET /api/payments/order/{orderId}
Get payments for a specific order. (Authenticated)

---

## 🔍 Search Endpoints

### GET /api/search/products
Search products with filters.

**Query Parameters:**
- `q`: Search query
- `category`: Category filter
- `minPrice`: Minimum price
- `maxPrice`: Maximum price
- `page`: Page number
- `size`: Page size

### GET /api/search/suggest
Get search suggestions.

**Query Parameters:**
- `q`: Search query

---

## 🛍️ Checkout Endpoints

### POST /api/checkout/create-session
Create checkout session. (Authenticated)

**Request:**
```json
{
  "cartId": 1,
  "shippingAddress": {
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  },
  "billingAddress": {
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  }
}
```

### POST /api/checkout/confirm
Confirm checkout and create order. (Authenticated)

---

## 👤 User Management Endpoints

### GET /api/users/profile
Get user profile. (Authenticated)

### PUT /api/users/profile
Update user profile. (Authenticated)

**Request:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890"
}
```

### GET /api/users
Get all users. (Admin only)

### GET /api/users/{id}
Get user by ID. (Admin only)

### PUT /api/users/{id}/status
Update user status. (Admin only)

---

## 🏪 Vendor Endpoints

### GET /api/vendors
Get all vendors.

### GET /api/vendors/{id}
Get vendor by ID.

### POST /api/vendors
Create vendor. (Admin only)

### PUT /api/vendors/{id}
Update vendor. (Admin only)

---

## 📊 Analytics Endpoints

### GET /api/analytics/sales
Get sales analytics. (Admin only)

### GET /api/analytics/products
Get product analytics. (Admin only)

### GET /api/analytics/users
Get user analytics. (Admin only)

---

## 📦 Inventory Endpoints

### GET /api/inventory
Get inventory status. (Admin only)

### PUT /api/inventory/{productId}
Update product inventory. (Admin only)

**Request:**
```json
{
  "quantity": 100,
  "threshold": 10
}
```

---

## 🔔 Notification Endpoints

### GET /api/notifications
Get user notifications. (Authenticated)

### POST /api/notifications/send
Send notification. (Admin only)

### PUT /api/notifications/{id}/read
Mark notification as read. (Authenticated)

---

## 💰 Stripe Payment Endpoints

### POST /api/stripe/create-payment-intent
Create Stripe payment intent. (Authenticated)

### POST /api/stripe/confirm-payment
Confirm Stripe payment. (Authenticated)

### POST /api/stripe/webhook
Stripe webhook endpoint.

---

## 🏥 Health Check

### GET /api/health
Check application health.

**Response (200):**
```json
{
  "success": true,
  "message": "Service is healthy",
  "data": {
    "status": "UP",
    "timestamp": "2025-08-08T12:30:45"
  },
  "timestamp": "2025-08-08T12:30:45"
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "errors": [
      {
        "field": "email",
        "message": "Invalid email format"
      }
    ]
  },
  "timestamp": "2025-08-08T12:30:45"
}
```

### 401 Unauthorized
```json
{
  "success": false,
  "message": "Authentication required",
  "data": null,
  "timestamp": "2025-08-08T12:30:45"
}
```

### 403 Forbidden
```json
{
  "success": false,
  "message": "Insufficient permissions",
  "data": null,
  "timestamp": "2025-08-08T12:30:45"
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Resource not found",
  "data": null,
  "timestamp": "2025-08-08T12:30:45"
}
```

### 500 Internal Server Error
```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "timestamp": "2025-08-08T12:30:45"
}
```
