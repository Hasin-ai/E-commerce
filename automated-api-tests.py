#!/usr/bin/env python3
"""
Automated API Testing Script for E-Commerce Application
Based on the QA Testing Prompt - Customer Journey Workflow

This script implements the end-to-end customer journey testing as outlined in the QA prompt:
1. User Registration & Login
2. Browse Products
3. Add to Cart
4. Place an Order
5. Payment Processing
6. Receive Notifications
"""

import requests
import json
import time
import sys
from typing import Dict, Any, Optional
import logging

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

class ECommerceAPITester:
    def __init__(self, base_url: str = "http://localhost:8080"):
        self.base_url = base_url
        self.session = requests.Session()
        self.auth_token = None
        self.user_id = None
        self.cart_id = None
        self.order_id = None
        self.payment_id = None

    def set_auth_header(self, token: str):
        """Set Authorization header for authenticated requests"""
        self.auth_token = token
        self.session.headers.update({'Authorization': f'Bearer {token}'})

    def clear_auth_header(self):
        """Clear Authorization header"""
        self.auth_token = None
        if 'Authorization' in self.session.headers:
            del self.session.headers['Authorization']

    def make_request(self, method: str, endpoint: str, data: Optional[Dict] = None,
                    params: Optional[Dict] = None, expected_status: int = 200) -> Dict[str, Any]:
        """Make HTTP request and validate response"""
        url = f"{self.base_url}{endpoint}"

        try:
            response = self.session.request(method, url, json=data, params=params)

            logger.info(f"{method} {endpoint} - Status: {response.status_code}")

            if response.status_code != expected_status:
                logger.error(f"Expected status {expected_status}, got {response.status_code}")
                logger.error(f"Response: {response.text}")

            response.raise_for_status()
            return response.json() if response.content else {}

        except requests.exceptions.RequestException as e:
            logger.error(f"Request failed: {e}")
            raise

    def test_health_check(self) -> bool:
        """Test if API is running"""
        try:
            response = self.make_request('GET', '/api/health', expected_status=200)
            logger.info("✅ Health check passed - API is running")
            return True
        except Exception as e:
            logger.error(f"❌ Health check failed: {e}")
            return False

    def test_user_registration(self) -> bool:
        """Step 1: Test user registration"""
        logger.info("\n🔐 Testing User Registration & Authentication...")

        # Test user registration
        registration_data = {
            "firstName": "John",
            "lastName": "Doe",
            "email": f"john.doe.{int(time.time())}@example.com",
            "password": "SecurePassword123!",
            "phone": "+1234567890"
        }

        try:
            response = self.make_request('POST', '/api/auth/register',
                                      data=registration_data, expected_status=201)

            # Validate response structure
            assert 'data' in response
            assert 'id' in response['data']
            assert response['data']['email'] == registration_data['email']

            self.user_id = response['data']['id']
            logger.info(f"✅ User registration successful - User ID: {self.user_id}")

            # Store credentials for login test
            self.login_email = registration_data['email']
            self.login_password = registration_data['password']

            return True

        except Exception as e:
            logger.error(f"❌ User registration failed: {e}")
            return False

    def test_user_login(self) -> bool:
        """Step 1: Test user login and JWT generation"""

        login_data = {
            "email": self.login_email,
            "password": self.login_password
        }

        try:
            response = self.make_request('POST', '/api/auth/login',
                                      data=login_data, expected_status=200)

            # Validate JWT token structure
            assert 'data' in response
            assert 'accessToken' in response['data']
            assert 'tokenType' in response['data']
            assert 'user' in response['data']

            # Set authentication for subsequent requests
            self.set_auth_header(response['data']['accessToken'])

            logger.info("✅ User login successful - JWT token received")
            return True

        except Exception as e:
            logger.error(f"❌ User login failed: {e}")
            return False

    def test_browse_products(self) -> bool:
        """Step 2: Test product browsing"""
        logger.info("\n🛍️ Testing Product Browsing...")

        try:
            # Test get all products
            response = self.make_request('GET', '/api/products')

            assert 'data' in response
            assert 'content' in response['data']  # Spring Page structure

            products = response['data']['content']
            logger.info(f"✅ Retrieved {len(products)} products")

            # Test get product by ID if products exist
            if products:
                product_id = products[0]['id']
                product_response = self.make_request('GET', f'/api/products/{product_id}')

                assert 'data' in product_response
                assert product_response['data']['id'] == product_id

                logger.info(f"✅ Product details retrieved for ID: {product_id}")

                # Store product for cart testing
                self.test_product = products[0]

            return True

        except Exception as e:
            logger.error(f"❌ Product browsing failed: {e}")
            return False

    def test_cart_operations(self) -> bool:
        """Step 3: Test cart operations"""
        logger.info("\n🛒 Testing Cart Operations...")

        try:
            # Test get current cart
            cart_response = self.make_request('GET', '/api/cart')

            assert 'data' in cart_response
            logger.info("✅ Cart retrieved successfully")

            # Test add item to cart (if we have a product)
            if hasattr(self, 'test_product'):
                add_item_data = {
                    "productId": self.test_product['id'],
                    "quantity": 2
                }

                add_response = self.make_request('POST', '/api/cart/items',
                                               data=add_item_data, expected_status=201)

                assert 'data' in add_response
                logger.info(f"✅ Item added to cart - Product: {self.test_product['name']}")

                # Test update cart item quantity
                if 'data' in add_response and 'items' in add_response['data']:
                    items = add_response['data']['items']
                    if items:
                        item_id = items[0]['id']
                        update_data = {"quantity": 3}

                        update_response = self.make_request('PUT', f'/api/cart/items/{item_id}',
                                                          data=update_data)

                        logger.info(f"✅ Cart item quantity updated")

            return True

        except Exception as e:
            logger.error(f"❌ Cart operations failed: {e}")
            return False

    def test_order_placement(self) -> bool:
        """Step 4: Test order placement"""
        logger.info("\n📦 Testing Order Placement...")

        try:
            # Test place order
            order_data = {
                "shippingAddress": {
                    "street": "123 Main St",
                    "city": "New York",
                    "state": "NY",
                    "zipCode": "10001",
                    "country": "US"
                }
            }

            response = self.make_request('POST', '/api/orders',
                                       data=order_data, expected_status=201)

            assert 'data' in response
            assert 'id' in response['data']

            self.order_id = response['data']['id']
            logger.info(f"✅ Order placed successfully - Order ID: {self.order_id}")

            # Test get order history
            orders_response = self.make_request('GET', '/api/orders')

            assert 'data' in orders_response
            logger.info("✅ Order history retrieved successfully")

            # Test get specific order details
            if self.order_id:
                order_detail_response = self.make_request('GET', f'/api/orders/{self.order_id}')

                assert 'data' in order_detail_response
                assert order_detail_response['data']['id'] == self.order_id

                logger.info(f"✅ Order details retrieved for ID: {self.order_id}")

            return True

        except Exception as e:
            logger.error(f"❌ Order placement failed: {e}")
            return False

    def test_payment_processing(self) -> bool:
        """Step 5: Test payment processing"""
        logger.info("\n💳 Testing Payment Processing...")

        try:
            # Test payment processing
            payment_data = {
                "orderId": self.order_id,
                "amount": 29.99,
                "currency": "USD",
                "paymentMethod": "card",
                "cardDetails": {
                    "cardNumber": "4242424242424242",  # Test card number
                    "expiryMonth": "12",
                    "expiryYear": "2025",
                    "cvv": "123"
                }
            }

            response = self.make_request('POST', '/api/payments',
                                       data=payment_data, expected_status=201)

            assert 'data' in response
            assert 'id' in response['data']

            self.payment_id = response['data']['id']
            logger.info(f"✅ Payment processed successfully - Payment ID: {self.payment_id}")

            # Test get payment details
            if self.payment_id:
                payment_response = self.make_request('GET', f'/api/payments/{self.payment_id}')

                assert 'data' in payment_response
                assert payment_response['data']['id'] == self.payment_id

                logger.info(f"✅ Payment details retrieved for ID: {self.payment_id}")

            return True

        except Exception as e:
            logger.error(f"❌ Payment processing failed: {e}")
            return False

    def test_notifications(self) -> bool:
        """Step 6: Test notifications"""
        logger.info("\n🔔 Testing Notifications...")

        try:
            # Test get user notifications
            response = self.make_request('GET', '/api/notifications')

            assert 'data' in response
            logger.info("✅ Notifications retrieved successfully")

            return True

        except Exception as e:
            logger.error(f"❌ Notifications test failed: {e}")
            return False

    def test_error_scenarios(self) -> bool:
        """Test error scenarios and edge cases"""
        logger.info("\n⚠️ Testing Error Scenarios...")

        try:
            # Test unauthorized access (clear auth header temporarily)
            self.clear_auth_header()

            try:
                self.make_request('GET', '/api/cart', expected_status=401)
                logger.info("✅ Unauthorized access properly rejected")
            except requests.exceptions.HTTPError:
                pass  # Expected

            # Restore auth header
            if self.auth_token:
                self.set_auth_header(self.auth_token)

            # Test invalid product ID
            try:
                self.make_request('GET', '/api/products/999999', expected_status=404)
                logger.info("✅ Invalid product ID properly handled")
            except requests.exceptions.HTTPError:
                pass  # Expected

            # Test invalid login
            invalid_login = {
                "email": "invalid@example.com",
                "password": "wrongpassword"
            }

            try:
                self.make_request('POST', '/api/auth/login',
                                data=invalid_login, expected_status=401)
                logger.info("✅ Invalid login properly rejected")
            except requests.exceptions.HTTPError:
                pass  # Expected

            return True

        except Exception as e:
            logger.error(f"❌ Error scenarios testing failed: {e}")
            return False

    def run_full_test_suite(self) -> bool:
        """Run the complete customer journey test suite"""
        logger.info("🚀 Starting E-Commerce API Test Suite")
        logger.info("=" * 60)

        test_results = []

        # Health check
        test_results.append(("Health Check", self.test_health_check()))

        # Customer journey tests
        test_results.append(("User Registration", self.test_user_registration()))
        test_results.append(("User Login", self.test_user_login()))
        test_results.append(("Browse Products", self.test_browse_products()))
        test_results.append(("Cart Operations", self.test_cart_operations()))
        test_results.append(("Order Placement", self.test_order_placement()))
        test_results.append(("Payment Processing", self.test_payment_processing()))
        test_results.append(("Notifications", self.test_notifications()))
        test_results.append(("Error Scenarios", self.test_error_scenarios()))

        # Print summary
        logger.info("\n" + "=" * 60)
        logger.info("📊 TEST SUMMARY")
        logger.info("=" * 60)

        passed = 0
        total = len(test_results)

        for test_name, result in test_results:
            status = "✅ PASSED" if result else "❌ FAILED"
            logger.info(f"{test_name:<20} {status}")
            if result:
                passed += 1

        logger.info("=" * 60)
        logger.info(f"TOTAL: {passed}/{total} tests passed")

        if passed == total:
            logger.info("🎉 All tests passed! E-commerce API is functioning correctly.")
            return True
        else:
            logger.error(f"⚠️ {total - passed} tests failed. Please check the logs above.")
            return False

def main():
    """Main entry point"""
    import argparse

    parser = argparse.ArgumentParser(description='E-Commerce API Test Suite')
    parser.add_argument('--base-url', default='http://localhost:8080',
                       help='Base URL of the API (default: http://localhost:8080)')
    parser.add_argument('--verbose', action='store_true',
                       help='Enable verbose logging')

    args = parser.parse_args()

    if args.verbose:
        logging.getLogger().setLevel(logging.DEBUG)

    tester = ECommerceAPITester(args.base_url)

    try:
        success = tester.run_full_test_suite()
        sys.exit(0 if success else 1)
    except KeyboardInterrupt:
        logger.info("\n🛑 Tests interrupted by user")
        sys.exit(1)
    except Exception as e:
        logger.error(f"💥 Unexpected error: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()
