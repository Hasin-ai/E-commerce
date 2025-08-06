// Postman Collection Test Runner Script
// This script can be used in Postman's Collection Runner or Newman CLI

// Pre-request Script for Collection
const preRequestScript = `
// Set dynamic variables
pm.globals.set("timestamp", new Date().toISOString());
pm.globals.set("random_email", "test" + Math.floor(Math.random() * 10000) + "@example.com");
pm.globals.set("random_phone", "+1" + Math.floor(Math.random() * 9000000000 + 1000000000));

// Log request details for debugging
console.log("Request URL:", pm.request.url.toString());
console.log("Request Method:", pm.request.method);

// Add request timestamp
pm.request.headers.add({
    key: "X-Request-Timestamp",
    value: pm.globals.get("timestamp")
});
`;

// Test Script for Collection
const testScript = `
// Common test utilities
const utils = {
    // Check if response is successful
    isSuccessResponse: function() {
        return pm.response.code >= 200 && pm.response.code < 300;
    },
    
    // Get response JSON safely
    getResponseJson: function() {
        try {
            return pm.response.json();
        } catch (e) {
            console.error("Failed to parse response as JSON:", e);
            return null;
        }
    },
    
    // Check if response has expected structure
    hasExpectedStructure: function(response, expectedKeys) {
        if (!response) return false;
        return expectedKeys.every(key => response.hasOwnProperty(key));
    },
    
    // Store response data for next requests
    storeResponseData: function(response) {
        if (response && response.data) {
            // Store common IDs
            if (response.data.id) {
                pm.globals.set("last_response_id", response.data.id);
            }
            if (response.data.accessToken) {
                pm.globals.set("auth_token", response.data.accessToken);
            }
            if (response.data.user && response.data.user.id) {
                pm.globals.set("user_id", response.data.user.id);
            }
        }
    }
};

// Common tests for all endpoints
pm.test("Response time is acceptable", function () {
    pm.expect(pm.response.responseTime).to.be.below(5000);
});

pm.test("Response has correct content type", function () {
    pm.expect(pm.response.headers.get("Content-Type")).to.include("application/json");
});

// Success response tests
if (utils.isSuccessResponse()) {
    const response = utils.getResponseJson();
    
    if (response) {
        pm.test("Response has success structure", function () {
            pm.expect(response).to.have.property("success");
            pm.expect(response).to.have.property("message");
            pm.expect(response).to.have.property("timestamp");
        });
        
        // Store response data for subsequent requests
        utils.storeResponseData(response);
    }
} else {
    // Error response tests
    const response = utils.getResponseJson();
    
    if (response) {
        pm.test("Error response has proper structure", function () {
            pm.expect(response).to.have.property("success", false);
            pm.expect(response).to.have.property("message");
            pm.expect(response.message).to.be.a("string");
        });
    }
}

// Log response for debugging
console.log("Response Status:", pm.response.code);
console.log("Response Time:", pm.response.responseTime + "ms");
if (pm.response.code >= 400) {
    console.log("Error Response:", pm.response.text());
}
`;

// Environment setup for different stages
const environments = {
    development: {
        base_url: "http://localhost:8080",
        database_url: "jdbc:postgresql://localhost:5432/ecommerce_dev",
        stripe_publishable_key: "pk_test_...",
        debug_mode: true
    },
    staging: {
        base_url: "https://staging-api.ecommerce.com",
        database_url: "jdbc:postgresql://staging-db:5432/ecommerce_staging",
        stripe_publishable_key: "pk_test_...",
        debug_mode: true
    },
    production: {
        base_url: "https://api.ecommerce.com",
        database_url: "jdbc:postgresql://prod-db:5432/ecommerce_prod",
        stripe_publishable_key: "pk_live_...",
        debug_mode: false
    }
};

// Test data generators
const testDataGenerators = {
    generateUser: function() {
        const timestamp = Date.now();
        return {
            firstName: "Test",
            lastName: "User" + timestamp,
            email: "test" + timestamp + "@example.com",
            password: "SecurePassword123!",
            phone: "+1" + Math.floor(Math.random() * 9000000000 + 1000000000)
        };
    },
    
    generateProduct: function() {
        const timestamp = Date.now();
        return {
            name: "Test Product " + timestamp,
            description: "Test product description",
            sku: "TEST-" + timestamp,
            price: Math.floor(Math.random() * 1000) + 10,
            categoryId: 1,
            stockQuantity: Math.floor(Math.random() * 100) + 1,
            isActive: true,
            isFeatured: false
        };
    },
    
    generateAddress: function() {
        return {
            street: "123 Test Street",
            city: "Test City",
            state: "TS",
            zipCode: "12345",
            country: "US"
        };
    }
};

// Newman CLI command examples
const newmanCommands = {
    runCollection: "newman run postman-collection.json -e environment.json",
    runWithReports: "newman run postman-collection.json -e environment.json --reporters cli,html --reporter-html-export report.html",
    runSpecificFolder: "newman run postman-collection.json -e environment.json --folder 'Auth Domain'",
    runWithIterations: "newman run postman-collection.json -e environment.json -n 5",
    runWithDelay: "newman run postman-collection.json -e environment.json --delay-request 1000"
};

// Export for use in Node.js environments
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        preRequestScript,
        testScript,
        environments,
        testDataGenerators,
        newmanCommands
    };
}

console.log("Postman Test Runner Script loaded successfully");