# Shopping Cart API - Comprehensive Test Cases Documentation

**Project:** bank5 - Shopping Cart Application  
**JIRA Issue:** SCRUM-11692  
**API Version:** 1.0.0  
**Test Suite Version:** 1.0.0  
**Last Updated:** 2024-01-15  
**Base URL:** http://localhost:8080/api  
**Authentication:** Basic Auth (username: testuser, password: password)

---

## Table of Contents
1. [Test Environment Setup](#test-environment-setup)
2. [API Endpoints Overview](#api-endpoints-overview)
3. [Add Item to Cart - Positive Tests](#add-item-to-cart---positive-tests)
4. [Add Item to Cart - Negative Tests](#add-item-to-cart---negative-tests)
5. [Get Cart - Positive Tests](#get-cart---positive-tests)
6. [Get Cart - Negative Tests](#get-cart---negative-tests)
7. [Update Item Quantity - Positive Tests](#update-item-quantity---positive-tests)
8. [Update Item Quantity - Negative Tests](#update-item-quantity---negative-tests)
9. [Remove Item from Cart - Positive Tests](#remove-item-from-cart---positive-tests)
10. [Remove Item from Cart - Negative Tests](#remove-item-from-cart---negative-tests)
11. [Performance Tests](#performance-tests)
12. [Edge Cases](#edge-cases)
13. [Test Execution Summary](#test-execution-summary)

---

## Test Environment Setup

### Prerequisites
- Java 21 installed
- Maven 3.6+ installed
- Application running on http://localhost:8080
- Postman or Newman for test execution
- H2 database initialized with sample products

### Sample Products Available
1. **Wireless Mouse** - ID: 550e8400-e29b-41d4-a716-446655440000, Price: $49.99, Stock: 100
2. **Mechanical Keyboard** - Price: $129.99, Stock: 50
3. **USB-C Cable** - Price: $19.99, Stock: 200
4. **Laptop Stand** - Price: $79.99, Stock: 75
5. **Webcam HD** - Price: $89.99, Stock: 30

### Authentication Details
- **Type:** Basic Authentication
- **Username:** testuser
- **Password:** password

---

## API Endpoints Overview

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| /api/cart/add | POST | Add item to cart | Yes |
| /api/cart | GET | Get current cart | Yes |
| /api/cart/{productId} | PUT | Update item quantity | Yes |
| /api/cart/{productId} | DELETE | Remove item from cart | Yes |

---

## Add Item to Cart - Positive Tests

### TC001: Add Single Item Successfully

**Test Case ID:** TC001  
**Priority:** High  
**Category:** Positive Test  
**Endpoint:** POST /api/cart/add

**Objective:** Verify that a user can successfully add a single item to their cart

**Preconditions:**
- User is authenticated
- Product exists in database
- Product has sufficient stock

**Test Data:**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": 2
}
```

**Test Steps:**
1. Authenticate with valid credentials (testuser/password)
2. Send POST request to /api/cart/add with valid product ID and quantity
3. Verify response status code
4. Verify response body structure
5. Verify cart contains the added item
6. Verify total price is calculated correctly

**Expected Result:**
- Status Code: 200 OK
- Response contains:
  - Valid cart ID
  - User ID matching authenticated user
  - Items array with added product
  - Correct total price (quantity × price)
  - Item count = 1
- Response time < 500ms

**Validation Checks:**
- ✓ Response has 'id' field
- ✓ Response has 'userId' field
- ✓ Response has 'items' array
- ✓ Response has 'totalPrice' field
- ✓ Response has 'itemCount' field
- ✓ Items array length > 0
- ✓ Total price > 0
- ✓ Response time < 500ms

---

### TC002: Add Multiple Quantities

**Test Case ID:** TC002  
**Priority:** High  
**Category:** Positive Test  
**Endpoint:** POST /api/cart/add

**Objective:** Verify that multiple quantities of a product can be added to cart

**Preconditions:**
- User is authenticated
- Product exists with sufficient stock (≥5 units)

**Test Data:**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": 5
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with quantity = 5
3. Verify response status and structure
4. Verify quantity is correctly set to 5
5. Verify subtotal = price × 5

**Expected Result:**
- Status Code: 200 OK
- Item quantity = 5
- Subtotal = price × 5
- Total price reflects the quantity

**Validation Checks:**
- ✓ Status code is 200
- ✓ Item quantity matches requested quantity
- ✓ Subtotal calculation is correct
- ✓ Total price is updated

---

### TC003: Add Existing Product (Increment Quantity)

**Test Case ID:** TC003  
**Priority:** High  
**Category:** Positive Test  
**Endpoint:** POST /api/cart/add

**Objective:** Verify that adding an existing product increments its quantity instead of creating duplicate entries

**Preconditions:**
- User is authenticated
- Product already exists in cart with quantity = 2
- Product has sufficient stock for additional quantity

**Test Data:**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": 1
}
```

**Test Steps:**
1. Add product to cart with quantity = 2 (setup)
2. Add same product again with quantity = 1
3. Verify cart contains only one entry for the product
4. Verify quantity is incremented to 3
5. Verify total price is recalculated

**Expected Result:**
- Status Code: 200 OK
- Cart contains single entry for product
- Product quantity = 3 (2 + 1)
- Total price reflects updated quantity
- No duplicate product entries

**Validation Checks:**
- ✓ Status code is 200
- ✓ Items array contains product
- ✓ No duplicate entries for same product
- ✓ Quantity is correctly incremented

---

## Add Item to Cart - Negative Tests

### TC004: Add Item with Invalid Product ID

**Test Case ID:** TC004  
**Priority:** High  
**Category:** Negative Test  
**Endpoint:** POST /api/cart/add

**Objective:** Verify proper error handling when attempting to add a non-existent product

**Preconditions:**
- User is authenticated

**Test Data:**
```json
{
  "productId": "invalid-product-id-12345",
  "quantity": 2
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with invalid product ID
3. Verify error response
4. Verify error structure and content

**Expected Result:**
- Status Code: 404 Not Found
- Error Response contains:
  - timestamp
  - traceId
  - errorCode: "PRODUCT_NOT_FOUND"
  - message: "Product not found"

**Validation Checks:**
- ✓ Status code is 404
- ✓ Error response has correct structure
- ✓ Error code is PRODUCT_NOT_FOUND
- ✓ Error message is descriptive

---

### TC005: Add Item with Null Product ID

**Test Case ID:** TC005  
**Priority:** High  
**Category:** Negative Test - Validation  
**Endpoint:** POST /api/cart/add

**Objective:** Verify validation error when product ID is null

**Preconditions:**
- User is authenticated

**Test Data:**
```json
{
  "productId": null,
  "quantity": 2
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with null product ID
3. Verify validation error response
4. Verify error details contain field-level validation

**Expected Result:**
- Status Code: 400 Bad Request
- Error Response:
  - errorCode: "VALIDATION_ERROR"
  - message: "Validation failed"
  - details array contains field validation error

**Validation Checks:**
- ✓ Status code is 400
- ✓ Error code is VALIDATION_ERROR
- ✓ Details array is present
- ✓ Field validation error for productId

---

### TC006: Add Item with Quantity Zero

**Test Case ID:** TC006  
**Priority:** High  
**Category:** Negative Test - Validation  
**Endpoint:** POST /api/cart/add

**Objective:** Verify validation error when quantity is less than minimum (1)

**Preconditions:**
- User is authenticated

**Test Data:**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": 0
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with quantity = 0
3. Verify validation error

**Expected Result:**
- Status Code: 400 Bad Request
- Error: "Quantity must be at least 1"
- errorCode: "VALIDATION_ERROR"

**Validation Checks:**
- ✓ Status code is 400
- ✓ Validation error for quantity field
- ✓ Error message indicates minimum value requirement

---

### TC007: Add Item with Quantity Exceeding Maximum

**Test Case ID:** TC007  
**Priority:** High  
**Category:** Negative Test - Validation  
**Endpoint:** POST /api/cart/add

**Objective:** Verify validation error when quantity exceeds maximum allowed (100)

**Preconditions:**
- User is authenticated

**Test Data:**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": 101
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with quantity = 101
3. Verify validation error

**Expected Result:**
- Status Code: 400 Bad Request
- Error: "Quantity cannot exceed 100"
- errorCode: "VALIDATION_ERROR"

**Validation Checks:**
- ✓ Status code is 400
- ✓ Validation error for quantity field
- ✓ Error message indicates maximum value constraint

---

### TC008: Add Item Exceeding Stock

**Test Case ID:** TC008  
**Priority:** High  
**Category:** Negative Test - Business Logic  
**Endpoint:** POST /api/cart/add

**Objective:** Verify out-of-stock error when requested quantity exceeds available stock

**Preconditions:**
- User is authenticated
- Product has limited stock (e.g., 100 units)

**Test Data:**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": 999
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with quantity exceeding stock
3. Verify out-of-stock error

**Expected Result:**
- Status Code: 409 Conflict
- Error Response:
  - errorCode: "OUT_OF_STOCK"
  - message: "Product out of stock" or "Insufficient stock"

**Validation Checks:**
- ✓ Status code is 409
- ✓ Error code is OUT_OF_STOCK
- ✓ Error message is descriptive

---

### TC009: Add Item Without Authentication

**Test Case ID:** TC009  
**Priority:** Critical  
**Category:** Negative Test - Security  
**Endpoint:** POST /api/cart/add

**Objective:** Verify that unauthenticated requests are rejected

**Preconditions:**
- No authentication credentials provided

**Test Data:**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": 2
}
```

**Test Steps:**
1. Send POST request without authentication header
2. Verify unauthorized error

**Expected Result:**
- Status Code: 401 Unauthorized
- Request is rejected
- No cart modification occurs

**Validation Checks:**
- ✓ Status code is 401
- ✓ Request is denied
- ✓ No data is modified

---

## Get Cart - Positive Tests

### TC010: Get Cart Successfully

**Test Case ID:** TC010  
**Priority:** High  
**Category:** Positive Test  
**Endpoint:** GET /api/cart

**Objective:** Verify that authenticated user can retrieve their cart

**Preconditions:**
- User is authenticated
- User has items in cart

**Test Steps:**
1. Authenticate with valid credentials
2. Send GET request to /api/cart
3. Verify response structure
4. Verify cart data is correct

**Expected Result:**
- Status Code: 200 OK
- Response contains:
  - Cart ID
  - User ID
  - Items array with cart items
  - Total price
  - Item count
- Response time < 500ms

**Validation Checks:**
- ✓ Status code is 200
- ✓ Cart has valid structure
- ✓ Items array is present
- ✓ Total price is calculated
- ✓ Response time is acceptable

---

### TC011: Get Empty Cart

**Test Case ID:** TC011  
**Priority:** Medium  
**Category:** Positive Test  
**Endpoint:** GET /api/cart

**Objective:** Verify behavior when retrieving an empty cart

**Preconditions:**
- User is authenticated
- User has no items in cart

**Test Steps:**
1. Authenticate with valid credentials
2. Ensure cart is empty (remove all items if necessary)
3. Send GET request to /api/cart
4. Verify empty cart response

**Expected Result:**
- Status Code: 200 OK or 404 Not Found (depending on implementation)
- If 200: Empty cart with itemCount = 0, totalPrice = 0
- If 404: Cart not found error

**Validation Checks:**
- ✓ Status code is 200 or 404
- ✓ If 200: itemCount = 0 and totalPrice = 0
- ✓ Items array is empty

---

## Get Cart - Negative Tests

### TC012: Get Cart Without Authentication

**Test Case ID:** TC012  
**Priority:** Critical  
**Category:** Negative Test - Security  
**Endpoint:** GET /api/cart

**Objective:** Verify that unauthenticated requests cannot access cart data

**Preconditions:**
- No authentication credentials provided

**Test Steps:**
1. Send GET request without authentication header
2. Verify unauthorized error

**Expected Result:**
- Status Code: 401 Unauthorized
- Request is rejected
- No cart data is returned

**Validation Checks:**
- ✓ Status code is 401
- ✓ Request is denied
- ✓ No sensitive data is exposed

---

## Update Item Quantity - Positive Tests

### TC013: Update Quantity Successfully

**Test Case ID:** TC013  
**Priority:** High  
**Category:** Positive Test  
**Endpoint:** PUT /api/cart/{productId}

**Objective:** Verify that item quantity can be updated successfully

**Preconditions:**
- User is authenticated
- Product exists in cart
- New quantity is within valid range and stock limits

**Test Data:**
```json
{
  "quantity": 3
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Add product to cart (setup)
3. Send PUT request to update quantity to 3
4. Verify quantity is updated
5. Verify total price is recalculated

**Expected Result:**
- Status Code: 200 OK
- Item quantity updated to 3
- Subtotal = price × 3
- Total price recalculated

**Validation Checks:**
- ✓ Status code is 200
- ✓ Quantity is updated
- ✓ Total price is recalculated
- ✓ Cart structure is valid

---

### TC014: Update to Minimum Quantity

**Test Case ID:** TC014  
**Priority:** Medium  
**Category:** Positive Test - Boundary  
**Endpoint:** PUT /api/cart/{productId}

**Objective:** Verify that quantity can be updated to minimum allowed value (1)

**Preconditions:**
- User is authenticated
- Product exists in cart with quantity > 1

**Test Data:**
```json
{
  "quantity": 1
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Ensure product is in cart with quantity > 1
3. Send PUT request to update quantity to 1
4. Verify quantity is set to 1

**Expected Result:**
- Status Code: 200 OK
- Item quantity = 1
- Total price reflects single unit

**Validation Checks:**
- ✓ Status code is 200
- ✓ Quantity is set to 1
- ✓ Minimum boundary is accepted

---

### TC015: Update to Maximum Quantity

**Test Case ID:** TC015  
**Priority:** Medium  
**Category:** Positive Test - Boundary  
**Endpoint:** PUT /api/cart/{productId}

**Objective:** Verify that quantity can be updated to maximum allowed (within stock limits)

**Preconditions:**
- User is authenticated
- Product exists in cart
- Product has sufficient stock (≥10 units)

**Test Data:**
```json
{
  "quantity": 10
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request to update quantity to 10
3. Verify quantity is updated (if stock allows)

**Expected Result:**
- Status Code: 200 OK (if stock sufficient) or 409 Conflict (if stock insufficient)
- If 200: Quantity updated to 10
- If 409: Out of stock error

**Validation Checks:**
- ✓ Status code is 200 or 409
- ✓ If 200: quantity is updated
- ✓ Stock validation is enforced

---

## Update Item Quantity - Negative Tests

### TC016: Update with Invalid Product ID

**Test Case ID:** TC016  
**Priority:** High  
**Category:** Negative Test  
**Endpoint:** PUT /api/cart/{productId}

**Objective:** Verify error handling when updating quantity for non-existent product

**Preconditions:**
- User is authenticated

**Test Data:**
```json
{
  "quantity": 5
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request with invalid product ID
3. Verify error response

**Expected Result:**
- Status Code: 404 Not Found
- Error: Product not found in cart

**Validation Checks:**
- ✓ Status code is 404
- ✓ Error response structure is valid
- ✓ Error message is descriptive

---

### TC017: Update with Zero Quantity

**Test Case ID:** TC017  
**Priority:** High  
**Category:** Negative Test - Validation  
**Endpoint:** PUT /api/cart/{productId}

**Objective:** Verify validation error when updating quantity to zero

**Preconditions:**
- User is authenticated
- Product exists in cart

**Test Data:**
```json
{
  "quantity": 0
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request with quantity = 0
3. Verify validation error

**Expected Result:**
- Status Code: 400 Bad Request
- Error: "Quantity must be at least 1"
- errorCode: "VALIDATION_ERROR"

**Validation Checks:**
- ✓ Status code is 400
- ✓ Validation error is returned
- ✓ Error indicates minimum value requirement

---

### TC018: Update Exceeding Stock

**Test Case ID:** TC018  
**Priority:** High  
**Category:** Negative Test - Business Logic  
**Endpoint:** PUT /api/cart/{productId}

**Objective:** Verify out-of-stock error when updating quantity beyond available stock

**Preconditions:**
- User is authenticated
- Product exists in cart
- Product has limited stock

**Test Data:**
```json
{
  "quantity": 999
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request with quantity exceeding stock
3. Verify out-of-stock error

**Expected Result:**
- Status Code: 409 Conflict
- Error: "Requested quantity exceeds available stock"
- errorCode: "OUT_OF_STOCK"

**Validation Checks:**
- ✓ Status code is 409
- ✓ Error code is OUT_OF_STOCK
- ✓ Stock validation is enforced

---

### TC019: Update Exceeding Maximum Allowed

**Test Case ID:** TC019  
**Priority:** High  
**Category:** Negative Test - Validation  
**Endpoint:** PUT /api/cart/{productId}

**Objective:** Verify validation error when quantity exceeds maximum (100)

**Preconditions:**
- User is authenticated
- Product exists in cart

**Test Data:**
```json
{
  "quantity": 101
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send PUT request with quantity = 101
3. Verify validation error

**Expected Result:**
- Status Code: 400 Bad Request
- Error: "Quantity cannot exceed 100"
- errorCode: "VALIDATION_ERROR"

**Validation Checks:**
- ✓ Status code is 400
- ✓ Validation error for exceeding max
- ✓ Error message indicates constraint

---

### TC020: Update Without Authentication

**Test Case ID:** TC020  
**Priority:** Critical  
**Category:** Negative Test - Security  
**Endpoint:** PUT /api/cart/{productId}

**Objective:** Verify that unauthenticated requests cannot update cart

**Preconditions:**
- No authentication credentials provided

**Test Data:**
```json
{
  "quantity": 5
}
```

**Test Steps:**
1. Send PUT request without authentication header
2. Verify unauthorized error

**Expected Result:**
- Status Code: 401 Unauthorized
- Request is rejected
- No cart modification occurs

**Validation Checks:**
- ✓ Status code is 401
- ✓ Request is denied
- ✓ No data is modified

---

## Remove Item from Cart - Positive Tests

### TC021: Remove Item Successfully

**Test Case ID:** TC021  
**Priority:** High  
**Category:** Positive Test  
**Endpoint:** DELETE /api/cart/{productId}

**Objective:** Verify that an item can be successfully removed from cart

**Preconditions:**
- User is authenticated
- Product exists in cart

**Test Steps:**
1. Authenticate with valid credentials
2. Add product to cart (setup)
3. Send DELETE request to remove product
4. Verify item is removed
5. Verify total price is recalculated
6. Verify item count is updated

**Expected Result:**
- Status Code: 200 OK
- Item removed from cart
- Total price recalculated
- Item count decremented

**Validation Checks:**
- ✓ Status code is 200
- ✓ Item is removed from items array
- ✓ Total price is recalculated
- ✓ Item count is updated

---

### TC022: Remove Last Item from Cart

**Test Case ID:** TC022  
**Priority:** Medium  
**Category:** Positive Test  
**Endpoint:** DELETE /api/cart/{productId}

**Objective:** Verify cart state when removing the last item

**Preconditions:**
- User is authenticated
- Cart contains only one item

**Test Steps:**
1. Authenticate with valid credentials
2. Ensure cart has only one item
3. Send DELETE request to remove the item
4. Verify cart is empty

**Expected Result:**
- Status Code: 200 OK
- Cart is empty (itemCount = 0)
- Total price = 0
- Items array is empty

**Validation Checks:**
- ✓ Status code is 200
- ✓ Cart is empty after removal
- ✓ Total price is 0
- ✓ Item count is 0

---

## Remove Item from Cart - Negative Tests

### TC023: Remove Non-Existent Item

**Test Case ID:** TC023  
**Priority:** High  
**Category:** Negative Test  
**Endpoint:** DELETE /api/cart/{productId}

**Objective:** Verify error handling when attempting to remove non-existent item

**Preconditions:**
- User is authenticated
- Product does not exist in cart

**Test Steps:**
1. Authenticate with valid credentials
2. Send DELETE request for non-existent product
3. Verify error response

**Expected Result:**
- Status Code: 404 Not Found
- Error: Product not found in cart

**Validation Checks:**
- ✓ Status code is 404
- ✓ Error response is returned
- ✓ Error message is descriptive

---

### TC024: Remove Item Without Authentication

**Test Case ID:** TC024  
**Priority:** Critical  
**Category:** Negative Test - Security  
**Endpoint:** DELETE /api/cart/{productId}

**Objective:** Verify that unauthenticated requests cannot remove items

**Preconditions:**
- No authentication credentials provided

**Test Steps:**
1. Send DELETE request without authentication header
2. Verify unauthorized error

**Expected Result:**
- Status Code: 401 Unauthorized
- Request is rejected
- No cart modification occurs

**Validation Checks:**
- ✓ Status code is 401
- ✓ Request is denied
- ✓ No data is modified

---

## Performance Tests

### TC025: Response Time Test for Add Item

**Test Case ID:** TC025  
**Priority:** Medium  
**Category:** Performance Test  
**Endpoint:** POST /api/cart/add

**Objective:** Verify that add item operation meets performance requirements

**Preconditions:**
- User is authenticated
- System is under normal load

**Test Data:**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": 1
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request to add item
3. Measure response time

**Expected Result:**
- Status Code: 200 OK
- Response time < 500ms (as per LLD requirement)

**Validation Checks:**
- ✓ Status code is 200
- ✓ Response time < 500ms
- ✓ Operation completes successfully

---

### TC026: Response Time Test for Get Cart

**Test Case ID:** TC026  
**Priority:** Medium  
**Category:** Performance Test  
**Endpoint:** GET /api/cart

**Objective:** Verify that get cart operation meets performance requirements

**Preconditions:**
- User is authenticated
- Cart contains items
- System is under normal load

**Test Steps:**
1. Authenticate with valid credentials
2. Send GET request to retrieve cart
3. Measure response time

**Expected Result:**
- Status Code: 200 OK
- Response time < 500ms

**Validation Checks:**
- ✓ Status code is 200
- ✓ Response time < 500ms
- ✓ Cart data is returned

---

## Edge Cases

### TC027: Add Item with Boundary Quantity (1)

**Test Case ID:** TC027  
**Priority:** Medium  
**Category:** Edge Case - Boundary Test  
**Endpoint:** POST /api/cart/add

**Objective:** Verify that minimum allowed quantity (1) is accepted

**Preconditions:**
- User is authenticated
- Product exists with stock ≥ 1

**Test Data:**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": 1
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with quantity = 1
3. Verify item is added successfully

**Expected Result:**
- Status Code: 200 OK
- Item added with quantity = 1
- Minimum boundary is accepted

**Validation Checks:**
- ✓ Status code is 200
- ✓ Minimum quantity is accepted
- ✓ Item is added to cart

---

### TC028: Add Item with Negative Quantity

**Test Case ID:** TC028  
**Priority:** Medium  
**Category:** Edge Case - Invalid Input  
**Endpoint:** POST /api/cart/add

**Objective:** Verify that negative quantity is rejected

**Preconditions:**
- User is authenticated

**Test Data:**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": -5
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with negative quantity
3. Verify validation error

**Expected Result:**
- Status Code: 400 Bad Request
- Error: Validation error for quantity
- errorCode: "VALIDATION_ERROR"

**Validation Checks:**
- ✓ Status code is 400
- ✓ Validation error is returned
- ✓ Negative values are rejected

---

### TC029: Add Item with Empty Product ID

**Test Case ID:** TC029  
**Priority:** Medium  
**Category:** Edge Case - Invalid Input  
**Endpoint:** POST /api/cart/add

**Objective:** Verify that empty product ID is rejected

**Preconditions:**
- User is authenticated

**Test Data:**
```json
{
  "productId": "",
  "quantity": 2
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send POST request with empty product ID
3. Verify error response

**Expected Result:**
- Status Code: 400 Bad Request or 404 Not Found
- Error response is returned

**Validation Checks:**
- ✓ Status code is 400 or 404
- ✓ Error response is returned
- ✓ Empty values are rejected

---

### TC030: Concurrent Add Operations

**Test Case ID:** TC030  
**Priority:** Low  
**Category:** Edge Case - Concurrency  
**Endpoint:** POST /api/cart/add

**Objective:** Verify cart state consistency under concurrent operations

**Preconditions:**
- User is authenticated
- Multiple requests sent simultaneously

**Test Data:**
```json
{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": 1
}
```

**Test Steps:**
1. Authenticate with valid credentials
2. Send multiple concurrent POST requests
3. Verify cart state is consistent
4. Verify no data corruption

**Expected Result:**
- Status Code: 200 OK for all requests
- Cart state is consistent
- Total price is correct
- No duplicate entries

**Validation Checks:**
- ✓ All requests succeed
- ✓ Cart state is consistent
- ✓ No data corruption
- ✓ Quantities are correctly aggregated

---

## Test Execution Summary

### Test Coverage

| Category | Total Tests | Description |
|----------|-------------|-------------|
| Positive Tests | 9 | Happy path scenarios |
| Negative Tests | 15 | Error handling and validation |
| Performance Tests | 2 | Response time verification |
| Edge Cases | 4 | Boundary and special conditions |
| **TOTAL** | **30** | **Complete test suite** |

### Endpoint Coverage

| Endpoint | Test Cases | Coverage |
|----------|------------|----------|
| POST /api/cart/add | 12 | 40% |
| GET /api/cart | 3 | 10% |
| PUT /api/cart/{productId} | 8 | 27% |
| DELETE /api/cart/{productId} | 5 | 17% |
| Performance & Edge Cases | 6 | 20% |

### Test Execution Checklist

- [ ] Environment setup completed
- [ ] Application is running on http://localhost:8080
- [ ] H2 database is initialized with sample products
- [ ] Authentication credentials verified
- [ ] Postman collection imported
- [ ] Environment variables configured
- [ ] All 30 test cases executed
- [ ] Test results documented
- [ ] Defects logged (if any)
- [ ] Test report generated

### Expected Test Results

**Success Criteria:**
- All positive tests (9) should pass
- All negative tests (15) should return expected error codes
- Performance tests (2) should meet < 500ms requirement
- Edge cases (4) should be handled correctly
- Overall pass rate: 100%

### Defect Reporting Template

**Defect ID:** [Auto-generated]  
**Test Case ID:** [TC###]  
**Severity:** [Critical/High/Medium/Low]  
**Priority:** [P1/P2/P3/P4]  
**Status:** [Open/In Progress/Resolved/Closed]  
**Environment:** [Test/Staging/Production]  
**Description:** [Detailed description]  
**Steps to Reproduce:** [Step-by-step]  
**Expected Result:** [What should happen]  
**Actual Result:** [What actually happened]  
**Attachments:** [Screenshots, logs, etc.]

---

## Test Automation Notes

### Newman Execution Command

```bash
newman run collection.json -e environment.json --reporters cli,html --reporter-html-export test-report.html
```

### CI/CD Integration

The test suite can be integrated into CI/CD pipeline:

```yaml
# Example GitHub Actions workflow
name: API Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Start Application
        run: |
          cd code
          mvn spring-boot:run &
          sleep 30
      - name: Run API Tests
        run: |
          npm install -g newman
          newman run test/postman/collection.json -e test/postman/environment.json
```

### Test Data Management

- Use environment variables for sensitive data
- Reset test data before each test run
- Use unique identifiers for test products
- Clean up test data after execution

---

## Appendix

### A. HTTP Status Codes Reference

| Status Code | Meaning | Usage in API |
|-------------|---------|-------------|
| 200 OK | Success | Successful operations |
| 400 Bad Request | Validation error | Invalid input data |
| 401 Unauthorized | Authentication failed | Missing/invalid credentials |
| 404 Not Found | Resource not found | Product/cart not found |
| 409 Conflict | Business logic error | Out of stock |
| 500 Internal Server Error | Server error | Unexpected failures |

### B. Error Codes Reference

| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| PRODUCT_NOT_FOUND | 404 | Product does not exist |
| OUT_OF_STOCK | 409 | Insufficient stock |
| CART_NOT_FOUND | 404 | Cart does not exist |
| CART_OPERATION_ERROR | 400 | Cart operation failed |
| VALIDATION_ERROR | 400 | Input validation failed |
| INTERNAL_ERROR | 500 | Unexpected error |

### C. Test Environment URLs

| Environment | Base URL | Purpose |
|-------------|----------|----------|
| Local | http://localhost:8080/api | Development testing |
| Staging | https://staging-api.bank5.com | Pre-production testing |
| Production | https://api.bank5.com | Production (read-only tests) |

### D. Contact Information

**QA Team Lead:** [Name]  
**Email:** qa-team@bank5.com  
**Slack Channel:** #qa-shopping-cart  
**JIRA Project:** SCRUM-11692

---

**Document Version:** 1.0.0  
**Last Updated:** 2024-01-15  
**Next Review Date:** 2024-02-15  
**Approved By:** QA Manager

---

*End of Test Cases Documentation*