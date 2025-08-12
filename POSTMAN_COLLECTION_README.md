# E-Commerce API Postman Collection

This comprehensive Postman collection provides complete testing coverage for the E-Commerce API built with Clean Architecture principles.

## 📋 Collection Overview

The collection includes **50+ endpoints** organized into the following domains:

### 🔍 Health Check (4 endpoints)
- Basic health check
- Detailed health check with system information
- Readiness probe for Kubernetes
- Liveness probe for monitoring

### 🔐 Authentication (3 endpoints)
- User registration with validation
- User login with JWT token generation
- User logout

### 👤 User Management (3 endpoints)
- Get current user profile
- Get user by ID
- Update user profile

### 🛍️ Product Management (6 endpoints)
- List all products with pagination and filters
- Get product by ID
- Get product by slug
- Get products by category
- Get featured products
- Search products with advanced filters

### 🔍 Search & Recommendations (3 endpoints)
- Advanced product search with filters and sorting
- Get personalized recommendations
- Get search suggestions/autocomplete

### 🛒 Shopping Cart (5 endpoints)
- Get current cart
- Add items to cart
- Update item quantities
- Remove items from cart
- Clear entire cart
- Get cart item count

### 🛒 Checkout (1 endpoint)
- Complete checkout workflow (cart → order → payment)

### 📦 Order Management (3 endpoints)
- Create new order
- Get user's order history
- Get specific order details

### 💳 Payment Processing (2 endpoints)
- Process payment with Stripe integration
- Get payment details

### 📊 Analytics (2 endpoints)
- Track user behavior events
- Admin dashboard analytics

### 🔔 Notifications (3 endpoints)
- Get user notifications
- Get unread notification count
- Mark notifications as read

### ⚠️ Error Scenarios (6 endpoints)
- Invalid product ID (404)
- Unauthorized access (401)
- Invalid credentials (401)
- Duplicate email registration (400)
- Invalid product in cart (404)
- Empty cart order creation (400)

## 🚀 Quick Start

### 1. Import Collection
1. Open Postman
2. Click "Import" button
3. Select `postman-collection.json`
4. Collection will be imported with all endpoints and tests

### 2. Set Environment Variables
Create a new environment with these variables:

```json
{
  "base_url": "http://localhost:8080",
  "auth_token": "",
  "admin_token": "",
  "user_id": "",
  "product_id": "",
  "cart_item_id": "",
  "order_id": "",
  "payment_id": "",
  "test_email": "",
  "test_password": ""
}
```

### 3. Run the Collection
1. **Manual Testing**: Click through endpoints individually
2. **Collection Runner**: Use Postman's Collection Runner for automated testing
3. **Newman CLI**: Run from command line (see Newman section below)

## 🔄 Test Flow

The collection is designed to run in sequence:

1. **Health Check** → Verify API is running
2. **User Registration** → Create test user
3. **User Login** → Get authentication token
4. **Product Discovery** → Browse and search products
5. **Cart Management** → Add items to cart
6. **Checkout** → Complete purchase workflow
7. **Order Tracking** → View order details
8. **Analytics** → Track user behavior

## 🧪 Automated Testing

### Pre-request Scripts
Each request includes automatic:
- Dynamic email generation for user registration
- Timestamp addition for request tracking
- Token management for authenticated endpoints

### Test Scripts
Comprehensive test coverage includes:
- Response time validation (< 5 seconds)
- HTTP status code verification
- Response structure validation
- Data type checking
- Business logic validation
- Error handling verification

### Example Test Results
```javascript
✓ API is healthy
✓ Response time is acceptable (245ms)
✓ User registration successful
✓ Response has correct structure
✓ JWT token structure is valid
✓ Products retrieved successfully
✓ Cart operations work correctly
✓ Payment processing successful
```

## 🖥️ Newman CLI Usage

### Installation
```bash
npm install -g newman
```

### Basic Commands
```bash
# Run entire collection
newman run postman-collection.json -e environment.json

# Run with HTML report
newman run postman-collection.json -e environment.json \
  --reporters cli,html --reporter-html-export report.html

# Run specific folder
newman run postman-collection.json -e environment.json \
  --folder "Auth Domain"

# Run with multiple iterations
newman run postman-collection.json -e environment.json -n 5

# Run with delay between requests
newman run postman-collection.json -e environment.json \
  --delay-request 1000
```

### CI/CD Integration
```yaml
# GitHub Actions example
- name: Run API Tests
  run: |
    newman run postman-collection.json \
      -e environment.json \
      --reporters cli,junit \
      --reporter-junit-export results.xml
```

## 🔧 Environment Configuration

### Development
```json
{
  "base_url": "http://localhost:8080",
  "database_url": "jdbc:postgresql://localhost:5432/ecommerce_dev",
  "stripe_publishable_key": "pk_test_...",
  "debug_mode": true
}
```

### Staging
```json
{
  "base_url": "https://staging-api.ecommerce.com",
  "database_url": "jdbc:postgresql://staging-db:5432/ecommerce_staging",
  "stripe_publishable_key": "pk_test_...",
  "debug_mode": true
}
```

### Production
```json
{
  "base_url": "https://api.ecommerce.com",
  "database_url": "jdbc:postgresql://prod-db:5432/ecommerce_prod",
  "stripe_publishable_key": "pk_live_...",
  "debug_mode": false
}
```

## 📊 Test Data Management

### Dynamic Data Generation
The collection automatically generates:
- Unique email addresses for user registration
- Random phone numbers
- Timestamps for tracking
- Test product data

### Test Data Cleanup
After testing, clean up test data:
```sql
-- Remove test users
DELETE FROM users WHERE email LIKE 'test%@example.com';

-- Remove test orders
DELETE FROM orders WHERE user_id IN (
  SELECT id FROM users WHERE email LIKE 'test%@example.com'
);
```

## 🔍 Debugging

### Enable Debug Mode
1. Set `debug_mode: true` in environment
2. Check Postman Console for detailed logs
3. Review request/response data in tests

### Common Issues
1. **401 Unauthorized**: Check if auth token is set correctly
2. **404 Not Found**: Verify endpoint URLs and IDs
3. **400 Bad Request**: Check request body format and required fields
4. **500 Internal Server Error**: Check server logs and database connection

## 📈 Performance Testing

### Load Testing with Newman
```bash
# Run 100 iterations with 10 concurrent users
newman run postman-collection.json -e environment.json \
  -n 100 --parallel 10

# Monitor response times
newman run postman-collection.json -e environment.json \
  --reporters cli,json --reporter-json-export results.json
```

### Performance Benchmarks
- Health endpoints: < 50ms
- Authentication: < 200ms
- Product queries: < 300ms
- Cart operations: < 150ms
- Order creation: < 500ms
- Payment processing: < 1000ms

## 🔒 Security Testing

### Authentication Tests
- ✅ Invalid credentials rejection
- ✅ Token expiration handling
- ✅ Unauthorized access prevention
- ✅ Role-based access control

### Input Validation Tests
- ✅ SQL injection prevention
- ✅ XSS attack prevention
- ✅ Invalid data format handling
- ✅ Required field validation

## 📝 Reporting

### HTML Reports
Generate comprehensive HTML reports:
```bash
newman run postman-collection.json -e environment.json \
  --reporters html --reporter-html-export report.html
```

### JUnit Reports (for CI/CD)
```bash
newman run postman-collection.json -e environment.json \
  --reporters junit --reporter-junit-export results.xml
```

### Custom Reports
Use the included `postman-test-runner.js` for custom reporting and test utilities.

## 🤝 Contributing

### Adding New Tests
1. Create new request in appropriate folder
2. Add comprehensive test scripts
3. Update environment variables if needed
4. Test manually before committing

### Test Standards
- All endpoints must have response time tests
- Include both positive and negative test cases
- Validate response structure and data types
- Add meaningful test descriptions

## 📚 Additional Resources

- [Postman Documentation](https://learning.postman.com/)
- [Newman CLI Documentation](https://github.com/postmanlabs/newman)
- [API Documentation](./E-commerce%20API%20Documentation.md)
- [Test Runner Script](./postman-test-runner.js)

## 🐛 Troubleshooting

### Common Solutions
1. **Collection not importing**: Check JSON format validity
2. **Tests failing**: Verify environment variables are set
3. **Authentication issues**: Ensure login request runs first
4. **Database errors**: Check if application is running and database is accessible

### Support
For issues with the collection:
1. Check the troubleshooting section
2. Review API documentation
3. Check server logs
4. Contact the development team

---

**Last Updated**: January 2025  
**Collection Version**: 2.0.0  
**API Version**: 1.0.0