<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" class="logo" width="120"/>

# E-commerce Backend - Clean Architecture Implementation Guide

## Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture Overview](#architecture-overview)
3. [Project Structure](#project-structure)
4. [Technology Stack](#technology-stack)
5. [Getting Started](#getting-started)
6. [Implementation Guide](#implementation-guide)
7. [Module Implementation Example](#module-implementation-example)
8. [API Documentation](#api-documentation)
9. [Testing Strategy](#testing-strategy)
10. [Deployment](#deployment)

## Project Overview

This is a comprehensive e-commerce backend application built using **Spring Boot** following **Clean Architecture** principles. The system provides REST APIs for managing users, products, orders, payments, notifications, and more.

### Key Features

- **User Management**: Registration, authentication, profile management
- **Product Catalog**: Product management, categories, inventory tracking
- **Shopping Cart**: Cart management and persistence
- **Order Processing**: Order creation, status tracking, invoicing
- **Payment Integration**: Multiple payment gateways, transaction management
- **Search \& Recommendations**: Product search, personalized recommendations
- **Notifications**: Email, SMS, push notifications
- **Analytics**: User behavior tracking, reporting


## Architecture Overview

This project follows **Clean Architecture** principles with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                    External Systems                          │
│              (Database, APIs, File Storage)                  │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                Infrastructure Layer                          │
│           (Config, Security, External Services)             │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                Interface Adapters                            │
│              (Controllers, DTOs, Mappers)                   │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                  Use Cases Layer                             │
│            (Application Business Logic)                     │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                  Domain Layer                                │
│         (Entities, Value Objects, Repositories)             │
└─────────────────────────────────────────────────────────────┘
```


### Architecture Benefits

- **Independence**: Core business logic is independent of frameworks and external systems
- **Testability**: Each layer can be tested in isolation
- **Maintainability**: Changes in one layer don't affect others
- **Scalability**: Easy to add new features without breaking existing code
- **Flexibility**: Can swap implementations without affecting business logic


## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── ecommerce/
│   │           ├── EcommerceApplication.java
│   │           │
│   │           ├── core/                          # DOMAIN & USE CASE LAYERS
│   │           │   ├── domain/                    # Business Entities & Rules
│   │           │   │   ├── user/
│   │           │   │   │   ├── entity/
│   │           │   │   │   ├── valueobject/
│   │           │   │   │   └── repository/
│   │           │   │   ├── product/
│   │           │   │   ├── cart/
│   │           │   │   ├── order/
│   │           │   │   ├── payment/
│   │           │   │   ├── notification/
│   │           │   │   ├── search/
│   │           │   │   ├── analytics/
│   │           │   │   └── recommendation/
│   │           │   │
│   │           │   └── usecase/                   # APPLICATION BUSINESS LOGIC
│   │           │       ├── user/
│   │           │       ├── product/
│   │           │       ├── cart/
│   │           │       └── [other modules]/
│   │           │
│   │           ├── adapter/                       # INTERFACE ADAPTERS
│   │           │   ├── web/                       # Web Layer
│   │           │   │   ├── controller/
│   │           │   │   ├── dto/
│   │           │   │   ├── mapper/
│   │           │   │   └── exception/
│   │           │   │
│   │           │   └── persistence/              # Persistence Layer
│   │           │       ├── jpa/
│   │           │       ├── implementation/
│   │           │       └── mapper/
│   │           │
│   │           ├── infrastructure/               # INFRASTRUCTURE LAYER
│   │           │   ├── config/
│   │           │   ├── security/
│   │           │   ├── external/
│   │           │   ├── messaging/
│   │           │   └── monitoring/
│   │           │
│   │           └── shared/                       # SHARED COMPONENTS
│   │               ├── exception/
│   │               ├── util/
│   │               └── constant/
│   │
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-prod.yml
│       └── db/migration/
│
└── test/
    └── java/
        └── com/
            └── ecommerce/
```


## Technology Stack

### Core Technologies

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Security**
- **Spring Web**


### Database

- **PostgreSQL** (Primary database)
- **Redis** (Caching and session storage)
- **Elasticsearch** (Search functionality)


### External Services

- **Stripe/PayPal** (Payment processing)
- **SendGrid** (Email notifications)
- **Twilio** (SMS notifications)


### Development Tools

- **Maven** (Build tool)
- **Docker** (Containerization)
- **Flyway** (Database migration)
- **JUnit 5** (Testing)
- **Mockito** (Mocking)


## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- PostgreSQL 13+
- Redis 6+
- Docker (optional)


### Installation

1. **Clone the repository**

```bash
git clone https://github.com/your-org/ecommerce-backend.git
cd ecommerce-backend
```

2. **Set up environment variables**

```bash
cp .env.example .env
# Edit .env with your configuration
```

3. **Start required services**

```bash
docker-compose up -d postgres redis elasticsearch
```

4. **Run the application**

```bash
mvn clean install
mvn spring-boot:run
```

5. **Access the API**
    - API Base URL: `http://localhost:8080/api`
    - Swagger UI: `http://localhost:8080/swagger-ui.html`

## Implementation Guide

This section provides a step-by-step guide for implementing a new module following Clean Architecture principles.

### Step 1: Define Domain Layer

#### 1.1 Create Domain Entity

**Location**: `src/main/java/com/ecommerce/core/domain/[module]/entity/`

**Purpose**: Represents core business objects with business rules and invariants.

```java
// Example: Product.java
package com.ecommerce.core.domain.product.entity;

import com.ecommerce.core.domain.product.valueobject.SKU;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.shared.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.List;

public class Product {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private SKU sku;
    private Price basePrice;
    private Price salePrice;
    private boolean isActive;
    private boolean isFeatured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor
    public Product(String name, String slug, String description, 
                   SKU sku, Price basePrice) {
        validateName(name);
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.sku = sku;
        this.basePrice = basePrice;
        this.isActive = true;
        this.isFeatured = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Business methods
    public void updatePrice(Price newPrice) {
        if (newPrice.isNegative()) {
            throw new BusinessException("Price cannot be negative");
        }
        this.basePrice = newPrice;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isAvailable() {
        return isActive;
    }
    
    // Private validation methods
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("Product name cannot be empty");
        }
        if (name.length() > 255) {
            throw new BusinessException("Product name too long");
        }
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSlug() { return slug; }
    public String getDescription() { return description; }
    public SKU getSku() { return sku; }
    public Price getBasePrice() { return basePrice; }
    public Price getSalePrice() { return salePrice; }
    public boolean isActive() { return isActive; }
    public boolean isFeatured() { return isFeatured; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // Package-private setters for repository use
    void setId(Long id) { this.id = id; }
    void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
```


#### 1.2 Create Value Objects

**Location**: `src/main/java/com/ecommerce/core/domain/[module]/valueobject/`

**Purpose**: Immutable objects that describe characteristics of entities.

```java
// Example: SKU.java
package com.ecommerce.core.domain.product.valueobject;

import com.ecommerce.shared.exception.ValidationException;

public class SKU {
    private final String value;
    
    public SKU(String value) {
        validate(value);
        this.value = value.toUpperCase();
    }
    
    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("SKU cannot be empty");
        }
        if (!value.matches("^[A-Z0-9-]{3,20}$")) {
            throw new ValidationException("SKU must be 3-20 characters, alphanumeric and hyphens only");
        }
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SKU sku = (SKU) obj;
        return value.equals(sku.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    @Override
    public String toString() {
        return value;
    }
}
```

```java
// Example: Price.java
package com.ecommerce.core.domain.product.valueobject;

import com.ecommerce.shared.exception.ValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Price {
    private final BigDecimal amount;
    private final String currency;
    
    public Price(BigDecimal amount, String currency) {
        validate(amount, currency);
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency.toUpperCase();
    }
    
    public Price(String amount, String currency) {
        this(new BigDecimal(amount), currency);
    }
    
    private void validate(BigDecimal amount, String currency) {
        if (amount == null) {
            throw new ValidationException("Price amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Price cannot be negative");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new ValidationException("Currency cannot be empty");
        }
        if (!currency.matches("^[A-Z]{3}$")) {
            throw new ValidationException("Currency must be a valid 3-letter code");
        }
    }
    
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
    
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    public Price add(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new ValidationException("Cannot add prices with different currencies");
        }
        return new Price(this.amount.add(other.amount), this.currency);
    }
    
    public Price multiply(int quantity) {
        return new Price(this.amount.multiply(BigDecimal.valueOf(quantity)), this.currency);
    }
    
    // Getters
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Price price = (Price) obj;
        return amount.equals(price.amount) && currency.equals(price.currency);
    }
    
    @Override
    public int hashCode() {
        return amount.hashCode() + currency.hashCode();
    }
    
    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
```


#### 1.3 Create Repository Interface

**Location**: `src/main/java/com/ecommerce/core/domain/[module]/repository/`

**Purpose**: Defines data access contracts without implementation details.

```java
// Example: ProductRepository.java
package com.ecommerce.core.domain.product.repository;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.valueobject.SKU;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    
    Product save(Product product);
    
    Optional<Product> findById(Long id);
    
    Optional<Product> findBySlug(String slug);
    
    Optional<Product> findBySku(SKU sku);
    
    List<Product> findAll();
    
    List<Product> findByCategory(Long categoryId);
    
    List<Product> findActiveProducts();
    
    List<Product> findFeaturedProducts();
    
    List<Product> findByNameContaining(String name);
    
    boolean existsBySku(SKU sku);
    
    boolean existsBySlug(String slug);
    
    void deleteById(Long id);
    
    long count();
    
    // Pagination support
    List<Product> findAll(int page, int size);
    
    List<Product> findActiveProducts(int page, int size);
}
```


### Step 2: Define Use Cases Layer

#### 2.1 Create Use Case Classes

**Location**: `src/main/java/com/ecommerce/core/usecase/[module]/`

**Purpose**: Contains application-specific business rules and orchestrates domain entities.

```java
// Example: CreateProductUseCase.java
package com.ecommerce.core.usecase.product;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.domain.product.valueobject.SKU;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.shared.exception.BusinessException;

import java.math.BigDecimal;

public class CreateProductUseCase {
    
    private final ProductRepository productRepository;
    
    public CreateProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public CreateProductResponse execute(CreateProductRequest request) {
        // Validate input
        validateRequest(request);
        
        // Check business rules
        SKU sku = new SKU(request.getSku());
        if (productRepository.existsBySku(sku)) {
            throw new BusinessException("Product with SKU " + sku.getValue() + " already exists");
        }
        
        if (productRepository.existsBySlug(request.getSlug())) {
            throw new BusinessException("Product with slug " + request.getSlug() + " already exists");
        }
        
        // Create domain entity
        Price basePrice = new Price(request.getBasePrice(), request.getCurrency());
        Product product = new Product(
            request.getName(),
            request.getSlug(),
            request.getDescription(),
            sku,
            basePrice
        );
        
        // Set optional fields
        if (request.getSalePrice() != null) {
            Price salePrice = new Price(request.getSalePrice(), request.getCurrency());
            product.updateSalePrice(salePrice);
        }
        
        // Save product
        Product savedProduct = productRepository.save(product);
        
        // Return response
        return new CreateProductResponse(
            savedProduct.getId(),
            savedProduct.getName(),
            savedProduct.getSlug(),
            savedProduct.getSku().getValue(),
            savedProduct.getBasePrice().getAmount(),
            savedProduct.getBasePrice().getCurrency(),
            savedProduct.isActive(),
            savedProduct.getCreatedAt()
        );
    }
    
    private void validateRequest(CreateProductRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BusinessException("Product name is required");
        }
        if (request.getSlug() == null || request.getSlug().trim().isEmpty()) {
            throw new BusinessException("Product slug is required");
        }
        if (request.getSku() == null || request.getSku().trim().isEmpty()) {
            throw new BusinessException("Product SKU is required");
        }
        if (request.getBasePrice() == null) {
            throw new BusinessException("Base price is required");
        }
        if (request.getCurrency() == null || request.getCurrency().trim().isEmpty()) {
            throw new BusinessException("Currency is required");
        }
    }
    
    // Request and Response classes
    public static class CreateProductRequest {
        private String name;
        private String slug;
        private String description;
        private String sku;
        private BigDecimal basePrice;
        private BigDecimal salePrice;
        private String currency;
        
        // Constructors, getters, and setters
        public CreateProductRequest() {}
        
        public CreateProductRequest(String name, String slug, String description, 
                                  String sku, BigDecimal basePrice, String currency) {
            this.name = name;
            this.slug = slug;
            this.description = description;
            this.sku = sku;
            this.basePrice = basePrice;
            this.currency = currency;
        }
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        
        public BigDecimal getBasePrice() { return basePrice; }
        public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
        
        public BigDecimal getSalePrice() { return salePrice; }
        public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
        
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
    }
    
    public static class CreateProductResponse {
        private final Long id;
        private final String name;
        private final String slug;
        private final String sku;
        private final BigDecimal basePrice;
        private final String currency;
        private final boolean isActive;
        private final java.time.LocalDateTime createdAt;
        
        public CreateProductResponse(Long id, String name, String slug, String sku,
                                   BigDecimal basePrice, String currency, boolean isActive,
                                   java.time.LocalDateTime createdAt) {
            this.id = id;
            this.name = name;
            this.slug = slug;
            this.sku = sku;
            this.basePrice = basePrice;
            this.currency = currency;
            this.isActive = isActive;
            this.createdAt = createdAt;
        }
        
        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getSlug() { return slug; }
        public String getSku() { return sku; }
        public BigDecimal getBasePrice() { return basePrice; }
        public String getCurrency() { return currency; }
        public boolean isActive() { return isActive; }
        public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    }
}
```


#### 2.2 Create Additional Use Cases

```java
// Example: GetProductUseCase.java
package com.ecommerce.core.usecase.product;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.shared.exception.ResourceNotFoundException;

public class GetProductUseCase {
    
    private final ProductRepository productRepository;
    
    public GetProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public GetProductResponse execute(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        return new GetProductResponse(
            product.getId(),
            product.getName(),
            product.getSlug(),
            product.getDescription(),
            product.getSku().getValue(),
            product.getBasePrice().getAmount(),
            product.getSalePrice() != null ? product.getSalePrice().getAmount() : null,
            product.getBasePrice().getCurrency(),
            product.isActive(),
            product.isFeatured(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
    
    public GetProductResponse executeBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));
        
        return new GetProductResponse(
            product.getId(),
            product.getName(),
            product.getSlug(),
            product.getDescription(),
            product.getSku().getValue(),
            product.getBasePrice().getAmount(),
            product.getSalePrice() != null ? product.getSalePrice().getAmount() : null,
            product.getBasePrice().getCurrency(),
            product.isActive(),
            product.isFeatured(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
    
    // Response class
    public static class GetProductResponse {
        private final Long id;
        private final String name;
        private final String slug;
        private final String description;
        private final String sku;
        private final java.math.BigDecimal basePrice;
        private final java.math.BigDecimal salePrice;
        private final String currency;
        private final boolean isActive;
        private final boolean isFeatured;
        private final java.time.LocalDateTime createdAt;
        private final java.time.LocalDateTime updatedAt;
        
        public GetProductResponse(Long id, String name, String slug, String description,
                                String sku, java.math.BigDecimal basePrice, java.math.BigDecimal salePrice,
                                String currency, boolean isActive, boolean isFeatured,
                                java.time.LocalDateTime createdAt, java.time.LocalDateTime updatedAt) {
            this.id = id;
            this.name = name;
            this.slug = slug;
            this.description = description;
            this.sku = sku;
            this.basePrice = basePrice;
            this.salePrice = salePrice;
            this.currency = currency;
            this.isActive = isActive;
            this.isFeatured = isFeatured;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
        
        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getSlug() { return slug; }
        public String getDescription() { return description; }
        public String getSku() { return sku; }
        public java.math.BigDecimal getBasePrice() { return basePrice; }
        public java.math.BigDecimal getSalePrice() { return salePrice; }
        public String getCurrency() { return currency; }
        public boolean isActive() { return isActive; }
        public boolean isFeatured() { return isFeatured; }
        public java.time.LocalDateTime getCreatedAt() { return createdAt; }
        public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    }
}
```


### Step 3: Define Adapter Layer

#### 3.1 Create Web DTOs

**Location**: `src/main/java/com/ecommerce/adapter/web/dto/`

**Purpose**: Data Transfer Objects for API requests and responses.

```java
// Example: CreateProductRequestDto.java
package com.ecommerce.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CreateProductRequestDto {
    
    @NotBlank(message = "Product name is required")
    @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
    private String name;
    
    @NotBlank(message = "Product slug is required")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    private String slug;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @NotBlank(message = "SKU is required")
    @Pattern(regexp = "^[A-Z0-9-]{3,20}$", message = "SKU must be 3-20 characters, alphanumeric and hyphens only")
    private String sku;
    
    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be positive")
    @Digits(integer = 10, fraction = 2, message = "Base price must have at most 2 decimal places")
    @JsonProperty("basePrice")
    private BigDecimal basePrice;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Sale price must be positive")
    @Digits(integer = 10, fraction = 2, message = "Sale price must have at most 2 decimal places")
    @JsonProperty("salePrice")
    private BigDecimal salePrice;
    
    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter code")
    private String currency;
    
    @JsonProperty("isActive")
    private boolean isActive = true;
    
    @JsonProperty("isFeatured")
    private boolean isFeatured = false;
    
    // Constructors
    public CreateProductRequestDto() {}
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public boolean isFeatured() { return isFeatured; }
    public void setFeatured(boolean featured) { isFeatured = featured; }
}
```

```java
// Example: ProductResponseDto.java
package com.ecommerce.adapter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponseDto {
    
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String sku;
    
    @JsonProperty("basePrice")
    private BigDecimal basePrice;
    
    @JsonProperty("salePrice")
    private BigDecimal salePrice;
    
    private String currency;
    
    @JsonProperty("isActive")
    private boolean isActive;
    
    @JsonProperty("isFeatured")
    private boolean isFeatured;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    // Constructors
    public ProductResponseDto() {}
    
    public ProductResponseDto(Long id, String name, String slug, String description,
                            String sku, BigDecimal basePrice, BigDecimal salePrice,
                            String currency, boolean isActive, boolean isFeatured,
                            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.sku = sku;
        this.basePrice = basePrice;
        this.salePrice = salePrice;
        this.currency = currency;
        this.isActive = isActive;
        this.isFeatured = isFeatured;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public boolean isFeatured() { return isFeatured; }
    public void setFeatured(boolean featured) { isFeatured = featured; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
```


#### 3.2 Create Mappers

**Location**: `src/main/java/com/ecommerce/adapter/web/mapper/`

**Purpose**: Convert between DTOs and use case request/response objects.

```java
// Example: ProductMapper.java
package com.ecommerce.adapter.web.mapper;

import com.ecommerce.adapter.web.dto.request.CreateProductRequestDto;
import com.ecommerce.adapter.web.dto.response.ProductResponseDto;
import com.ecommerce.core.usecase.product.CreateProductUseCase;
import com.ecommerce.core.usecase.product.GetProductUseCase;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    
    public CreateProductUseCase.CreateProductRequest toCreateProductRequest(CreateProductRequestDto dto) {
        CreateProductUseCase.CreateProductRequest request = new CreateProductUseCase.CreateProductRequest();
        request.setName(dto.getName());
        request.setSlug(dto.getSlug());
        request.setDescription(dto.getDescription());
        request.setSku(dto.getSku());
        request.setBasePrice(dto.getBasePrice());
        request.setSalePrice(dto.getSalePrice());
        request.setCurrency(dto.getCurrency());
        return request;
    }
    
    public ProductResponseDto toProductResponse(CreateProductUseCase.CreateProductResponse response) {
        return new ProductResponseDto(
            response.getId(),
            response.getName(),
            response.getSlug(),
            null, // description not included in create response
            response.getSku(),
            response.getBasePrice(),
            null, // sale price not included in create response
            response.getCurrency(),
            response.isActive(),
            false, // featured not included in create response
            response.getCreatedAt(),
            response.getCreatedAt() // updated at same as created at initially
        );
    }
    
    public ProductResponseDto toProductResponse(GetProductUseCase.GetProductResponse response) {
        return new ProductResponseDto(
            response.getId(),
            response.getName(),
            response.getSlug(),
            response.getDescription(),
            response.getSku(),
            response.getBasePrice(),
            response.getSalePrice(),
            response.getCurrency(),
            response.isActive(),
            response.isFeatured(),
            response.getCreatedAt(),
            response.getUpdatedAt()
        );
    }
}
```


#### 3.3 Create Controllers

**Location**: `src/main/java/com/ecommerce/adapter/web/controller/`

**Purpose**: Handle HTTP requests and responses.

```java
// Example: ProductController.java
package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.CreateProductRequestDto;
import com.ecommerce.adapter.web.dto.response.ProductResponseDto;
import com.ecommerce.adapter.web.mapper.ProductMapper;
import com.ecommerce.core.usecase.product.CreateProductUseCase;
import com.ecommerce.core.usecase.product.GetProductUseCase;
import com.ecommerce.shared.dto.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {
    
    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ProductMapper productMapper;
    
    @Autowired
    public ProductController(CreateProductUseCase createProductUseCase,
                           GetProductUseCase getProductUseCase,
                           ProductMapper productMapper) {
        this.createProductUseCase = createProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.productMapper = productMapper;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @Valid @RequestBody CreateProductRequestDto requestDto) {
        
        CreateProductUseCase.CreateProductRequest request = 
            productMapper.toCreateProductRequest(requestDto);
        
        CreateProductUseCase.CreateProductResponse response = 
            createProductUseCase.execute(request);
        
        ProductResponseDto responseDto = productMapper.toProductResponse(response);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(responseDto, "Product created successfully"));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProduct(
            @PathVariable @NotNull @Positive Long id) {
        
        GetProductUseCase.GetProductResponse response = 
            getProductUseCase.execute(id);
        
        ProductResponseDto responseDto = productMapper.toProductResponse(response);
        
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }
    
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductBySlug(
            @PathVariable @NotNull String slug) {
        
        GetProductUseCase.GetProductResponse response = 
            getProductUseCase.executeBySlug(slug);
        
        ProductResponseDto responseDto = productMapper.toProductResponse(response);
        
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }
}
```


### Step 4: Define Infrastructure Layer

#### 4.1 Create JPA Entities

**Location**: `src/main/java/com/ecommerce/adapter/persistence/jpa/entity/`

**Purpose**: Database table representations.

```java
// Example: ProductJpaEntity.java
package com.ecommerce.adapter.persistence.jpa.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class ProductJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "slug", nullable = false, unique = true, length = 255)
    private String slug;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "sku", nullable = false, unique = true, length = 50)
    private String sku;
    
    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;
    
    @Column(name = "sale_price", precision = 10, scale = 2)
    private BigDecimal salePrice;
    
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public ProductJpaEntity() {}
    
    public ProductJpaEntity(String name, String slug, String description, String sku,
                           BigDecimal basePrice, String currency) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.sku = sku;
        this.basePrice = basePrice;
        this.currency = currency;
        this.isActive = true;
        this.isFeatured = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
    
    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean featured) { isFeatured = featured; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
```


#### 4.2 Create JPA Repository

**Location**: `src/main/java/com/ecommerce/adapter/persistence/jpa/repository/`

**Purpose**: Spring Data JPA repositories for database operations.

```java
// Example: ProductJpaRepository.java
package com.ecommerce.adapter.persistence.jpa.repository;

import com.ecommerce.adapter.persistence.jpa.entity.ProductJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {
    
    Optional<ProductJpaEntity> findBySlug(String slug);
    
    Optional<ProductJpaEntity> findBySku(String sku);
    
    boolean existsBySlug(String slug);
    
    boolean existsBySku(String sku);
    
    List<ProductJpaEntity> findByIsActiveTrue();
    
    Page<ProductJpaEntity> findByIsActiveTrue(Pageable pageable);
    
    List<ProductJpaEntity> findByIsFeaturedTrue();
    
    Page<ProductJpaEntity> findByIsFeaturedTrue(Pageable pageable);
    
    List<ProductJpaEntity> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT p FROM ProductJpaEntity p WHERE p.isActive = true AND p.name LIKE %:name%")
    List<ProductJpaEntity> findActiveProductsByNameContaining(@Param("name") String name);
    
    @Query("SELECT p FROM ProductJpaEntity p JOIN p.categories c WHERE c.id = :categoryId")
    List<ProductJpaEntity> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT p FROM ProductJpaEntity p WHERE p.isActive = true AND p.basePrice BETWEEN :minPrice AND :maxPrice")
    List<ProductJpaEntity> findActiveProductsByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice, 
                                                          @Param("maxPrice") java.math.BigDecimal maxPrice);
}
```


#### 4.3 Create Repository Implementation

**Location**: `src/main/java/com/ecommerce/adapter/persistence/implementation/`

**Purpose**: Implement domain repository interfaces.

```java
// Example: ProductRepositoryImpl.java
package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.jpa.entity.ProductJpaEntity;
import com.ecommerce.adapter.persistence.jpa.repository.ProductJpaRepository;
import com.ecommerce.adapter.persistence.mapper.ProductEntityMapper;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.domain.product.valueobject.SKU;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
    private final ProductJpaRepository productJpaRepository;
    private final ProductEntityMapper productEntityMapper;
    
    @Autowired
    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository,
                               ProductEntityMapper productEntityMapper) {
        this.productJpaRepository = productJpaRepository;
        this.productEntityMapper = productEntityMapper;
    }
    
    @Override
    public Product save(Product product) {
        ProductJpaEntity jpaEntity = productEntityMapper.toJpaEntity(product);
        ProductJpaEntity savedEntity = productJpaRepository.save(jpaEntity);
        return productEntityMapper.toDomainEntity(savedEntity);
    }
    
    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id)
            .map(productEntityMapper::toDomainEntity);
    }
    
    @Override
    public Optional<Product> findBySlug(String slug) {
        return productJpaRepository.findBySlug(slug)
            .map(productEntityMapper::toDomainEntity);
    }
    
    @Override
    public Optional<Product> findBySku(SKU sku) {
        return productJpaRepository.findBySku(sku.getValue())
            .map(productEntityMapper::toDomainEntity);
    }
    
    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll()
            .stream()
            .map(productEntityMapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findByCategory(Long categoryId) {
        return productJpaRepository.findByCategoryId(categoryId)
            .stream()
            .map(productEntityMapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findActiveProducts() {
        return productJpaRepository.findByIsActiveTrue()
            .stream()
            .map(productEntityMapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findFeaturedProducts() {
        return productJpaRepository.findByIsFeaturedTrue()
            .stream()
            .map(productEntityMapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findByNameContaining(String name) {
        return productJpaRepository.findByNameContainingIgnoreCase(name)
            .stream()
            .map(productEntityMapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsBySku(SKU sku) {
        return productJpaRepository.existsBySku(sku.getValue());
    }
    
    @Override
    public boolean existsBySlug(String slug) {
        return productJpaRepository.existsBySlug(slug);
    }
    
    @Override
    public void deleteById(Long id) {
        productJpaRepository.deleteById(id);
    }
    
    @Override
    public long count() {
        return productJpaRepository.count();
    }
    
    @Override
    public List<Product> findAll(int page, int size) {
        org.springframework.data.domain.Pageable pageable = 
            org.springframework.data.domain.PageRequest.of(page, size);
        return productJpaRepository.findAll(pageable)
            .stream()
            .map(productEntityMapper::toDomainEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findActiveProducts(int page, int size) {
        org.springframework.data.domain.Pageable pageable = 
            org.springframework.data.domain.PageRequest.of(page, size);
        return productJpaRepository.findByIsActiveTrue(pageable)
            .stream()
            .map(productEntityMapper::toDomainEntity)
            .collect(Collectors.toList());
    }
}
```


#### 4.4 Create Entity Mapper

**Location**: `src/main/java/com/ecommerce/adapter/persistence/mapper/`

**Purpose**: Convert between domain entities and JPA entities.

```java
// Example: ProductEntityMapper.java
package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.adapter.persistence.jpa.entity.ProductJpaEntity;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.valueobject.SKU;
import com.ecommerce.core.domain.product.valueobject.Price;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ProductEntityMapper {
    
    public ProductJpaEntity toJpaEntity(Product product) {
        ProductJpaEntity jpaEntity = new ProductJpaEntity();
        
        jpaEntity.setId(product.getId());
        jpaEntity.setName(product.getName());
        jpaEntity.setSlug(product.getSlug());
        jpaEntity.setDescription(product.getDescription());
        jpaEntity.setSku(product.getSku().getValue());
        jpaEntity.setBasePrice(product.getBasePrice().getAmount());
        jpaEntity.setCurrency(product.getBasePrice().getCurrency());
        
        if (product.getSalePrice() != null) {
            jpaEntity.setSalePrice(product.getSalePrice().getAmount());
        }
        
        jpaEntity.setIsActive(product.isActive());
        jpaEntity.setIsFeatured(product.isFeatured());
        jpaEntity.setCreatedAt(product.getCreatedAt());
        jpaEntity.setUpdatedAt(product.getUpdatedAt());
        
        return jpaEntity;
    }
    
    public Product toDomainEntity(ProductJpaEntity jpaEntity) {
        // Create domain entity using reflection to set private fields
        try {
            SKU sku = new SKU(jpaEntity.getSku());
            Price basePrice = new Price(jpaEntity.getBasePrice(), jpaEntity.getCurrency());
            
            Product product = new Product(
                jpaEntity.getName(),
                jpaEntity.getSlug(),
                jpaEntity.getDescription(),
                sku,
                basePrice
            );
            
            // Set private fields using reflection
            setPrivateField(product, "id", jpaEntity.getId());
            setPrivateField(product, "isActive", jpaEntity.getIsActive());
            setPrivateField(product, "isFeatured", jpaEntity.getIsFeatured());
            setPrivateField(product, "createdAt", jpaEntity.getCreatedAt());
            setPrivateField(product, "updatedAt", jpaEntity.getUpdatedAt());
            
            if (jpaEntity.getSalePrice() != null) {
                Price salePrice = new Price(jpaEntity.getSalePrice(), jpaEntity.getCurrency());
                setPrivateField(product, "salePrice", salePrice);
            }
            
            return product;
            
        } catch (Exception e) {
            throw new RuntimeException("Error mapping JPA entity to domain entity", e);
        }
    }
    
    private void setPrivateField(Object object, String fieldName, Object value) 
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}
```


### Step 5: Configure Spring Boot

#### 5.1 Create Configuration Classes

**Location**: `src/main/java/com/ecommerce/infrastructure/config/`

**Purpose**: Wire dependencies and configure application components.

```java
// Example: ApplicationConfig.java
package com.ecommerce.infrastructure.config;

import com.ecommerce.adapter.persistence.implementation.ProductRepositoryImpl;
import com.ecommerce.adapter.persistence.mapper.ProductEntityMapper;
import com.ecommerce.adapter.persistence.jpa.repository.ProductJpaRepository;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.usecase.product.CreateProductUseCase;
import com.ecommerce.core.usecase.product.GetProductUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    
    @Bean
    public ProductRepository productRepository(ProductJpaRepository productJpaRepository,
                                             ProductEntityMapper productEntityMapper) {
        return new ProductRepositoryImpl(productJpaRepository, productEntityMapper);
    }
    
    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepository productRepository) {
        return new CreateProductUseCase(productRepository);
    }
    
    @Bean
    public GetProductUseCase getProductUseCase(ProductRepository productRepository) {
        return new GetProductUseCase(productRepository);
    }
}
```


#### 5.2 Create Shared Components

**Location**: `src/main/java/com/ecommerce/shared/`

**Purpose**: Common utilities and components used across the application.

```java
// Example: ApiResponse.java
package com.ecommerce.shared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ApiResponse<T> {
    
    private boolean success;
    private T data;
    private String message;
    private LocalDateTime timestamp;
    
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "Success");
    }
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }
    
    public static <T> ApiResponse<T> error(T data, String message) {
        return new ApiResponse<>(false, data, message);
    }
    
    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
```


## Module Implementation Example

Here's a complete example of implementing the **Product Module** following the Clean Architecture pattern:

### Step 1: Domain Layer Implementation

```java
// 1. Domain Entity
public class Product {
    // Implementation as shown above
}

// 2. Value Objects
public class SKU {
    // Implementation as shown above
}

public class Price {
    // Implementation as shown above
}

// 3. Repository Interface
public interface ProductRepository {
    // Implementation as shown above
}
```


### Step 2: Use Case Layer Implementation

```java
// 1. Create Product Use Case
public class CreateProductUseCase {
    // Implementation as shown above
}

// 2. Get Product Use Case
public class GetProductUseCase {
    // Implementation as shown above
}

// 3. Additional Use Cases
public class UpdateProductUseCase {
    // Similar implementation pattern
}

public class DeleteProductUseCase {
    // Similar implementation pattern
}
```


### Step 3: Adapter Layer Implementation

```java
// 1. Web DTOs
public class CreateProductRequestDto {
    // Implementation as shown above
}

public class ProductResponseDto {
    // Implementation as shown above
}

// 2. Web Mapper
@Component
public class ProductMapper {
    // Implementation as shown above
}

// 3. Web Controller
@RestController
@RequestMapping("/api/products")
public class ProductController {
    // Implementation as shown above
}
```


### Step 4: Infrastructure Layer Implementation

```java
// 1. JPA Entity
@Entity
@Table(name = "products")
public class ProductJpaEntity {
    // Implementation as shown above
}

// 2. JPA Repository
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {
    // Implementation as shown above
}

// 3. Repository Implementation
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    // Implementation as shown above
}

// 4. Entity Mapper
@Component
public class ProductEntityMapper {
    // Implementation as shown above
}
```


### Step 5: Configuration

```java
// 1. Application Configuration
@Configuration
public class ApplicationConfig {
    // Implementation as shown above
}

// 2. Database Migration
-- V1__create_products_table.sql
CREATE TABLE products (
    product_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    sku VARCHAR(50) UNIQUE NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    sale_price DECIMAL(10,2),
    currency VARCHAR(3) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_featured BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_slug ON products(slug);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_active ON products(is_active);
CREATE INDEX idx_products_featured ON products(is_featured);
```


## API Documentation

### Base URL

```
http://localhost:8080/api
```


### Authentication

Most endpoints require authentication using JWT tokens:

```
Authorization: Bearer <jwt_token>
```


### Product Management APIs

#### Create Product

```http
POST /api/products
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "name": "New Product",
  "slug": "new-product",
  "description": "Product description",
  "sku": "PRD-001",
  "basePrice": 99.99,
  "salePrice": 89.99,
  "currency": "USD",
  "isActive": true,
  "isFeatured": false
}
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "New Product",
    "slug": "new-product",
    "description": "Product description",
    "sku": "PRD-001",
    "basePrice": 99.99,
    "salePrice": 89.99,
    "currency": "USD",
    "isActive": true,
    "isFeatured": false,
    "createdAt": "2025-07-17T15:54:00Z",
    "updatedAt": "2025-07-17T15:54:00Z"
  },
  "message": "Product created successfully",
  "timestamp": "2025-07-17T15:54:00Z"
}
```


#### Get Product by ID

```http
GET /api/products/{id}
Authorization: Bearer <jwt_token>
```

**Response:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "New Product",
    "slug": "new-product",
    "description": "Product description",
    "sku": "PRD-001",
    "basePrice": 99.99,
    "salePrice": 89.99,
    "currency": "USD",
    "isActive": true,
    "isFeatured": false,
    "createdAt": "2025-07-17T15:54:00Z",
    "updatedAt": "2025-07-17T15:54:00Z"
  },
  "message": "Success",
  "timestamp": "2025-07-17T15:54:00Z"
}
```


#### Get Product by Slug

```http
GET /api/products/slug/{slug}
Authorization: Bearer <jwt_token>
```

**Response:** Same as Get Product by ID

## Testing Strategy

### Unit Tests

#### Domain Layer Tests

```java
// Example: ProductTest.java
package com.ecommerce.core.domain.product.entity;

import com.ecommerce.core.domain.product.valueobject.SKU;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    
    @Test
    public void shouldCreateValidProduct() {
        // Given
        String name = "Test Product";
        String slug = "test-product";
        String description = "Test description";
        SKU sku = new SKU("TEST-001");
        Price basePrice = new Price(new BigDecimal("99.99"), "USD");
        
        // When
        Product product = new Product(name, slug, description, sku, basePrice);
        
        // Then
        assertNotNull(product);
        assertEquals(name, product.getName());
        assertEquals(slug, product.getSlug());
        assertEquals(description, product.getDescription());
        assertEquals(sku, product.getSku());
        assertEquals(basePrice, product.getBasePrice());
        assertTrue(product.isActive());
        assertFalse(product.isFeatured());
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
    }
    
    @Test
    public void shouldThrowExceptionForInvalidName() {
        // Given
        String invalidName = "";
        String slug = "test-product";
        String description = "Test description";
        SKU sku = new SKU("TEST-001");
        Price basePrice = new Price(new BigDecimal("99.99"), "USD");
        
        // When & Then
        assertThrows(BusinessException.class, () -> {
            new Product(invalidName, slug, description, sku, basePrice);
        });
    }
    
    @Test
    public void shouldUpdatePrice() {
        // Given
        Product product = createValidProduct();
        Price newPrice = new Price(new BigDecimal("149.99"), "USD");
        
        // When
        product.updatePrice(newPrice);
        
        // Then
        assertEquals(newPrice, product.getBasePrice());
    }
    
    @Test
    public void shouldActivateProduct() {
        // Given
        Product product = createValidProduct();
        product.deactivate();
        
        // When
        product.activate();
        
        // Then
        assertTrue(product.isActive());
        assertTrue(product.isAvailable());
    }
    
    private Product createValidProduct() {
        return new Product(
            "Test Product",
            "test-product",
            "Test description",
            new SKU("TEST-001"),
            new Price(new BigDecimal("99.99"), "USD")
        );
    }
}
```


#### Use Case Tests

```java
// Example: CreateProductUseCaseTest.java
package com.ecommerce.core.usecase.product;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.domain.product.valueobject.SKU;
import com.ecommerce.shared.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateProductUseCaseTest {
    
    @Mock
    private ProductRepository productRepository;
    
    private CreateProductUseCase createProductUseCase;
    
    @BeforeEach
    public void setUp() {
        createProductUseCase = new CreateProductUseCase(productRepository);
    }
    
    @Test
    public void shouldCreateProductSuccessfully() {
        // Given
        CreateProductUseCase.CreateProductRequest request = new CreateProductUseCase.CreateProductRequest(
            "Test Product",
            "test-product",
            "Test description",
            "TEST-001",
            new BigDecimal("99.99"),
            "USD"
        );
        
        Product savedProduct = createMockProduct();
        
        when(productRepository.existsBySku(any(SKU.class))).thenReturn(false);
        when(productRepository.existsBySlug(anyString())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        
        // When
        CreateProductUseCase.CreateProductResponse response = createProductUseCase.execute(request);
        
        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Product", response.getName());
        assertEquals("test-product", response.getSlug());
        assertEquals("TEST-001", response.getSku());
        assertEquals(new BigDecimal("99.99"), response.getBasePrice());
        assertEquals("USD", response.getCurrency());
        assertTrue(response.isActive());
        
        verify(productRepository).existsBySku(any(SKU.class));
        verify(productRepository).existsBySlug(eq("test-product"));
        verify(productRepository).save(any(Product.class));
    }
    
    @Test
    public void shouldThrowExceptionWhenSkuAlreadyExists() {
        // Given
        CreateProductUseCase.CreateProductRequest request = new CreateProductUseCase.CreateProductRequest(
            "Test Product",
            "test-product",
            "Test description",
            "TEST-001",
            new BigDecimal("99.99"),
            "USD"
        );
        
        when(productRepository.existsBySku(any(SKU.class))).thenReturn(true);
        
        // When & Then
        assertThrows(BusinessException.class, () -> {
            createProductUseCase.execute(request);
        });
        
        verify(productRepository).existsBySku(any(SKU.class));
        verify(productRepository, never()).save(any(Product.class));
    }
    
    @Test
    public void shouldThrowExceptionForNullRequest() {
        // When & Then
        assertThrows(BusinessException.class, () -> {
            createProductUseCase.execute(null);
        });
        
        verify(productRepository, never()).save(any(Product.class));
    }
    
    private Product createMockProduct() {
        Product product = new Product(
            "Test Product",
            "test-product",
            "Test description",
            new SKU("TEST-001"),
            new Price(new BigDecimal("99.99"), "USD")
        );
        
        // Set ID using reflection (for test purposes)
        try {
            java.lang.reflect.Field idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(product, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return product;
    }
}
```


### Integration Tests

#### Controller Tests

```java
// Example: ProductControllerTest.java
package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.CreateProductRequestDto;
import com.ecommerce.adapter.web.dto.response.ProductResponseDto;
import com.ecommerce.adapter.web.mapper.ProductMapper;
import com.ecommerce.core.usecase.product.CreateProductUseCase;
import com.ecommerce.core.usecase.product.GetProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private CreateProductUseCase createProductUseCase;
    
    @MockBean
    private GetProductUseCase getProductUseCase;
    
    @MockBean
    private ProductMapper productMapper;
    
    @Test
    public void shouldCreateProductSuccessfully() throws Exception {
        // Given
        CreateProductRequestDto requestDto = new CreateProductRequestDto();
        requestDto.setName("Test Product");
        requestDto.setSlug("test-product");
        requestDto.setDescription("Test description");
        requestDto.setSku("TEST-001");
        requestDto.setBasePrice(new BigDecimal("99.99"));
        requestDto.setCurrency("USD");
        
        CreateProductUseCase.CreateProductResponse useCaseResponse = 
            new CreateProductUseCase.CreateProductResponse(
                1L, "Test Product", "test-product", "TEST-001",
                new BigDecimal("99.99"), "USD", true, LocalDateTime.now()
            );
        
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Test Product");
        responseDto.setSlug("test-product");
        responseDto.setSku("TEST-001");
        responseDto.setBasePrice(new BigDecimal("99.99"));
        responseDto.setCurrency("USD");
        responseDto.setActive(true);
        
        when(productMapper.toCreateProductRequest(any(CreateProductRequestDto.class)))
            .thenReturn(new CreateProductUseCase.CreateProductRequest());
        when(createProductUseCase.execute(any(CreateProductUseCase.CreateProductRequest.class)))
            .thenReturn(useCaseResponse);
        when(productMapper.toProductResponse(any(CreateProductUseCase.CreateProductResponse.class)))
            .thenReturn(responseDto);
        
        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test Product"))
                .andExpect(jsonPath("$.data.slug").value("test-product"))
                .andExpect(jsonPath("$.data.sku").value("TEST-001"))
                .andExpect(jsonPath("$.data.basePrice").value(99.99))
                .andExpect(jsonPath("$.data.currency").value("USD"))
                .andExpect(jsonPath("$.data.isActive").value(true))
                .andExpect(jsonPath("$.message").value("Product created successfully"));
    }
    
    @Test
    public void shouldReturnValidationErrorForInvalidRequest() throws Exception {
        // Given
        CreateProductRequestDto requestDto = new CreateProductRequestDto();
        // Missing required fields
        
        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void shouldGetProductById() throws Exception {
        // Given
        Long productId = 1L;
        
        GetProductUseCase.GetProductResponse useCaseResponse = 
            new GetProductUseCase.GetProductResponse(
                1L, "Test Product", "test-product", "Test description", "TEST-001",
                new BigDecimal("99.99"), null, "USD", true, false,
                LocalDateTime.now(), LocalDateTime.now()
            );
        
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Test Product");
        responseDto.setSlug("test-product");
        responseDto.setDescription("Test description");
        responseDto.setSku("TEST-001");
        responseDto.setBasePrice(new BigDecimal("99.99"));
        responseDto.setCurrency("USD");
        responseDto.setActive(true);
        
        when(getProductUseCase.execute(productId)).thenReturn(useCaseResponse);
        when(productMapper.toProductResponse(any(GetProductUseCase.GetProductResponse.class)))
            .thenReturn(responseDto);
        
        // When & Then
        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test Product"))
                .andExpect(jsonPath("$.data.slug").value("test-product"))
                .andExpect(jsonPath("$.data.description").value("Test description"))
                .andExpect(jsonPath("$.data.sku").value("TEST-001"))
                .andExpect(jsonPath("$.data.basePrice").value(99.99))
                .andExpect(jsonPath("$.data.currency").value("USD"))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }
}
```


#### Repository Tests

```java
// Example: ProductRepositoryImplTest.java
package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.jpa.entity.ProductJpaEntity;
import com.ecommerce.adapter.persistence.jpa.repository.ProductJpaRepository;
import com.ecommerce.adapter.persistence.mapper.ProductEntityMapper;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.valueobject.SKU;
import com.ecommerce.core.domain.product.valueobject.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryImplTest {
    
    @Mock
    private ProductJpaRepository productJpaRepository;
    
    @Mock
    private ProductEntityMapper productEntityMapper;
    
    @InjectMocks
    private ProductRepositoryImpl productRepository;
    
    @Test
    public void shouldSaveProduct() {
        // Given
        Product product = createTestProduct();
        ProductJpaEntity jpaEntity = createTestJpaEntity();
        ProductJpaEntity savedJpaEntity = createTestJpaEntity();
        savedJpaEntity.setId(1L);
        
        when(productEntityMapper.toJpaEntity(product)).thenReturn(jpaEntity);
        when(productJpaRepository.save(jpaEntity)).thenReturn(savedJpaEntity);
        when(productEntityMapper.toDomainEntity(savedJpaEntity)).thenReturn(product);
        
        // When
        Product savedProduct = productRepository.save(product);
        
        // Then
        assertNotNull(savedProduct);
        verify(productEntityMapper).toJpaEntity(product);
        verify(productJpaRepository).save(jpaEntity);
        verify(productEntityMapper).toDomainEntity(savedJpaEntity);
    }
    
    @Test
    public void shouldFindProductById() {
        // Given
        Long productId = 1L;
        ProductJpaEntity jpaEntity = createTestJpaEntity();
        Product product = createTestProduct();
        
        when(productJpaRepository.findById(productId)).thenReturn(Optional.of(jpaEntity));
        when(productEntityMapper.toDomainEntity(jpaEntity)).thenReturn(product);
        
        // When
        Optional<Product> foundProduct = productRepository.findById(productId);
        
        // Then
        assertTrue(foundProduct.isPresent());
        assertEquals(product, foundProduct.get());
        verify(productJpaRepository).findById(productId);
        verify(productEntityMapper).toDomainEntity(jpaEntity);
    }
    
    @Test
    public void shouldReturnEmptyWhenProductNotFound() {
        // Given
        Long productId = 1L;
        
        when(productJpaRepository.findById(productId)).thenReturn(Optional.empty());
        
        // When
        Optional<Product> foundProduct = productRepository.findById(productId);
        
        // Then
        assertFalse(foundProduct.isPresent());
        verify(productJpaRepository).findById(productId);
        verify(productEntityMapper, never()).toDomainEntity(any());
    }
    
    @Test
    public void shouldCheckIfSkuExists() {
        // Given
        SKU sku = new SKU("TEST-001");
        
        when(productJpaRepository.existsBySku(sku.getValue())).thenReturn(true);
        
        // When
        boolean exists = productRepository.existsBySku(sku);
        
        // Then
        assertTrue(exists);
        verify(productJpaRepository).existsBySku(sku.getValue());
    }
    
    private Product createTestProduct() {
        return new Product(
            "Test Product",
            "test-product",
            "Test description",
            new SKU("TEST-001"),
            new Price(new BigDecimal("99.99"), "USD")
        );
    }
    
    private ProductJpaEntity createTestJpaEntity() {
        ProductJpaEntity jpaEntity = new ProductJpaEntity();
        jpaEntity.setName("Test Product");
        jpaEntity.setSlug("test-product");
        jpaEntity.setDescription("Test description");
        jpaEntity.setSku("TEST-001");
        jpaEntity.setBasePrice(new BigDecimal("99.99"));
        jpaEntity.setCurrency("USD");
        jpaEntity.setIsActive(true);
        jpaEntity.setIsFeatured(false);
        return jpaEntity;
    }
}
```


### End-to-End Tests

```java
// Example: ProductE2ETest.java
package com.ecommerce.e2e;

import com.ecommerce.adapter.web.dto.request.CreateProductRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class ProductE2ETest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void shouldCreateAndRetrieveProduct() throws Exception {
        // Given
        CreateProductRequestDto requestDto = new CreateProductRequestDto();
        requestDto.setName("Integration Test Product");
        requestDto.setSlug("integration-test-product");
        requestDto.setDescription("Integration test description");
        requestDto.setSku("INT-001");
        requestDto.setBasePrice(new BigDecimal("149.99"));
        requestDto.setCurrency("USD");
        
        // When - Create product
        String createResponse = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Integration Test Product"))
                .andExpect(jsonPath("$.data.slug").value("integration-test-product"))
                .andExpect(jsonPath("$.data.sku").value("INT-001"))
                .andExpect(jsonPath("$.data.basePrice").value(149.99))
                .andExpect(jsonPath("$.data.currency").value("USD"))
                .andReturn().getResponse().getContentAsString();
        
        // Extract product ID from response
        Long productId = extractProductIdFromResponse(createResponse);
        
        // Then - Retrieve product
        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(productId))
                .andExpect(jsonPath("$.data.name").value("Integration Test Product"))
                .andExpect(jsonPath("$.data.slug").value("integration-test-product"))
                .andExpect(jsonPath("$.data.description").value("Integration test description"))
                .andExpect(jsonPath("$.data.sku").value("INT-001"))
                .andExpect(jsonPath("$.data.basePrice").value(149.99))
                .andExpect(jsonPath("$.data.currency").value("USD"))
                .andExpect(jsonPath("$.data.isActive").value(true));
    }
    
    private Long extractProductIdFromResponse(String response) {
        // Implementation to extract product ID from JSON response
        // This would use Jackson ObjectMapper to parse the response
        return 1L; // Simplified for example
    }
}
```


## Deployment

### Application Properties

```yaml
# application.yml
spring:
  application:
    name: ecommerce-backend
  
  profiles:
    active: dev
  
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce
    username: ${DB_USERNAME:ecommerce}
    password: ${DB_PASSWORD:password}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
  
  redis:
    host: localhost
    port: 6379
    password: ${REDIS_PASSWORD:}
    timeout: 2000ms
  
  cache:
    type: redis
    redis:
      time-to-live: 600000
  
  security:
    jwt:
      secret: ${JWT_SECRET:your-secret-key}
      expiration: 86400000 # 24 hours
      refresh-expiration: 604800000 # 7 days

server:
  port: 8080
  servlet:
    context-path: /api

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always

logging:
  level:
    com.ecommerce: INFO
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/ecommerce-backend.log

# Application-specific properties
ecommerce:
  cors:
    allowed-origins: http://localhost:3000,http://localhost:8080
    allowed-methods: GET,POST,PUT,DELETE,OPTIONS
    allowed-headers: "*"
    allow-credentials: true
  
  file-upload:
    max-file-size: 10MB
    max-request-size: 10MB
    upload-dir: ${UPLOAD_DIR:uploads/}
  
  external-services:
    stripe:
      api-key: ${STRIPE_API_KEY:sk_test_your_key}
      webhook-secret: ${STRIPE_WEBHOOK_SECRET:whsec_your_secret}
    
    sendgrid:
      api-key: ${SENDGRID_API_KEY:your_api_key}
      from-email: ${FROM_EMAIL:noreply@ecommerce.com}
    
    aws:
      s3:
        bucket-name: ${S3_BUCKET_NAME:ecommerce-files}
        region: ${AWS_REGION:us-east-1}
        access-key: ${AWS_ACCESS_KEY_ID:your_access_key}
        secret-key: ${AWS_SECRET_ACCESS_KEY:your_secret_key}
```


### Docker Configuration

```dockerfile
# Dockerfile
FROM openjdk:17-jdk-slim

VOLUME /tmp

# Add application jar
COPY target/ecommerce-backend-*.jar app.jar

# Add wait-for-it script for database connectivity
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Run application
ENTRYPOINT ["/wait-for-it.sh", "postgres:5432", "--", "java", "-jar", "/app.jar"]
```

```yaml
# docker-compose.yml
version: '3.8'

services:
  postgres:
    image: postgres:13
    environment:
      POSTGRES_DB: ecommerce
      POSTGRES_USER: ecommerce
      POSTGRES_PASSWORD: password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    networks:
      - ecommerce-network

  redis:
    image: redis:6-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - ecommerce-network

  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.15.0
    environment:
      - discovery.type=single-node
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ports:
      - "9200:9200"
    volumes:
      - elasticsearch_data:/usr/share/elasticsearch/data
    networks:
      - ecommerce-network

  ecommerce-backend:
    build: .
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - DB_HOST=postgres
      - DB_PORT=5432
      - DB_NAME=ecommerce
      - DB_USERNAME=ecommerce
      - DB_PASSWORD=password
      - REDIS_HOST=redis
      - REDIS_PORT=6

<div style="text-align: center">⁂</div>

[^1]: structure.md
[^2]: er_diagram.mermaid
[^3]: ecommerce_clean_architecture.md
[^4]: E-commerce-project-file-structure.md
[^5]: E-commerce-API-Documentation.md
[^6]: E-commerce-project-file-structure.md```

