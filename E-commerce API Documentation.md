# E-commerce API Documentation

This document provides a comprehensive overview of the E-commerce API, detailing available endpoints, request formats, and sample responses.

---

## 1. Authentication APIs

### `POST /api/auth/register`

**Description:** User registration.

**Request:**

```json
{
  "email": "john.doe@example.com",
  "password": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890"
}