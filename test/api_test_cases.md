# API Test Cases Documentation
## Shopping Cart API (SCRUM-11692)

**Project:** Shopping Cart Application  
**Repository:** bank5  
**Branch:** main  
**Generated:** 2024-01-15  
**Total Test Cases:** 20

---

## Table of Contents
1. [Test Environment Setup](#test-environment-setup)
2. [Add Item to Cart - Positive Tests](#add-item-to-cart---positive-tests)
3. [Add Item to Cart - Negative Tests](#add-item-to-cart---negative-tests)
4. [Get Cart - Positive Tests](#get-cart---positive-tests)
5. [Update Item Quantity - Positive Tests](#update-item-quantity---positive-tests)
6. [Update Item Quantity - Negative Tests](#update-item-quantity---negative-tests)
7. [Remove Item from Cart - Positive Tests](#remove-item-from-cart---positive-tests)
8. [Remove Item from Cart - Negative Tests](#remove-item-from-cart---negative-tests)
9. [Authentication Tests](#authentication-tests)
10. [Edge Cases and Boundary Tests](#edge-cases-and-boundary-tests)
11. [Test Execution Summary](#test-execution-summary)

---

## Test Environment Setup

### Prerequisites
- SpringBoot application running on `http://localhost:8080`
- H2 in-memory database initialized with sample products
- Basic authentication enabled (username: `testuser`, password: `password`)
- Postman or Newman installed for test execution

### Sample Data
| Product ID | Product Name | Price | Stock |
|------------|--------------|-------|-------|
| Auto-generated UUID | Wireless Mouse | $49.99 | 100 |
| Auto-generated UUID | Mechanical Keyboard | $129.99 | 50 |
| Auto-generated UUID | USB-C Cable | $19.99 | 200 |
| Auto-generated UUID | Laptop Stand | $79.99 | 75 |
| Auto-generated UUID | Webcam HD | $89.99 | 30 |

### Base URL
```
http://localhost:8080/api
```

### Authentication
- **Type:** HTTP Basic Authentication
- **Username:** testuser
- **Password:** password

---

## Add Item to Cart - Positive Tests

### TC001 - Add Single Item to Cart

**Test Case ID:** TC001  
**Priority:** High  
**Category:** Positive Test  
**Endpoint:** POST /api/cart/add

#### Objective
Verify that a user can successfully add a single item to their shopping cart with valid product ID and quantity.

#### Preconditions
- User is authenticated
- Product exists in database
- Product has sufficient stock

#### Test Steps
1. Send POST request to `/api/cart/add`
2. Include valid authentication credentials
3. Provide request body:
   ```json
   {
     "productId": "<valid-product-id>",
     "quantity": 2
   }
   ```
4. Verify response

#### Expected Result
- **Status Code:** 200 OK
- **Response Body:**
  ```json
  {
    "id": "<cart-id>",
    "userId": "testuser",
    "items": [
      {
        "id": "<item-id>",
        "productId": "<product-id>",
        "productName": "Wireless Mouse",
        "price": 49.99,
        "quantity": 2,
        "subtotal": 99.98
      }
    ],
    "totalPrice": 99.98,
    "itemCount": 1
  }
  ```
- Response time < 500ms
- Cart ID is generated and returned
- Item is added with correct quantity and price
- Total price is calculated correctly

#### Assertions
- ✅ Status code is 200
- ✅ Response has cart ID
- ✅ Response has userId matching authenticated user
- ✅ Items array contains the added product
- ✅ Quantity matches request
- ✅ Subtotal = price × quantity
- ✅ Total price is calculated correctly
- ✅ Item count is at least 1
- ✅ Response time < 500ms

---

### TC002 - Add Multiple Quantities

**Test Case ID:** TC002  
**Priority:** High  
**Category:** Positive Test  
**Endpoint:** POST /api/cart/add

#### Objective
Verify that a user can add an item with multiple quantities (quantity > 1).

#### Preconditions
- User is authenticated
- Product exists with sufficient stock

#### Test Steps
1. Send POST request to `/api/cart/add`
2. Provide request body with quantity = 5:
   ```json
   {
     "productId": "<valid-product-id>",
     "quantity": 5
   }
   ```
3. Verify response

#### Expected Result
- **Status Code:** 200 OK
- Item added with quantity = 5
- Subtotal = price × 5
- Total price updated correctly

#### Assertions
- ✅ Status code is 200
- ✅ Item quantity is 5
- ✅ Subtotal calculated correctly
- ✅ Total price includes new item

---

### TC003 - Add Same Item Again (Increment Quantity)

**Test Case ID:** TC003  
**Priority:** High  
**Category:** Positive Test  
**Endpoint:** POST /api/cart/add

#### Objective
Verify that adding the same product again increments the quantity instead of creating a duplicate entry.

#### Preconditions
- User is authenticated
- Product already exists in cart with quantity = 2

#### Test Steps
1. Send POST request to `/api/cart/add`
2. Provide same product ID with quantity = 1:
   ```json
   {
     "productId": "<existing-product-id>",
     "quantity": 1
   }
   ```
3. Verify response

#### Expected Result
- **Status Code:** 200 OK
- Existing item quantity incremented to 3
- No duplicate item created
- Subtotal and total price recalculated

#### Assertions
- ✅ Status code is 200
- ✅ Item count remains same (no duplicate)
- ✅ Quantity incremented correctly
- ✅ Subtotal updated
- ✅ Total price recalculated

---

## Add Item to Cart - Negative Tests

### TC004 - Add Item with Invalid Product ID

**Test Case ID:** TC004  
**Priority:** High  
**Category:** Negative Test  
**Endpoint:** POST /api/cart/add

#### Objective
Verify that the system returns appropriate error when attempting to add a non-existent product.

#### Preconditions
- User is authenticated

#### Test Steps
1. Send POST request to `/api/cart/add`
2. Provide invalid product ID:
   ```json
   {
     "productId": "invalid-product-id",
     "quantity": 2
   }
   ```
3. Verify error response

#### Expected Result
- **Status Code:** 404 Not Found
- **Error Response:**
  ```json
  {
    "timestamp": "2024-01-15T10:30:00",
    "traceId": "<uuid>",
    "errorCode": "PRODUCT_NOT_FOUND",
    "message": "Product not found"
  }
  ```

#### Assertions
- ✅ Status code is 404
- ✅ Error code is PRODUCT_NOT_FOUND
- ✅ Error message is descriptive
- ✅ Timestamp is present
- ✅ Trace ID is present for debugging

---

### TC005 - Add Item with Zero Quantity

**Test Case ID:** TC005  
**Priority:** High  
**Category:** Negative Test - Validation  
**Endpoint:** POST /api/cart/add

#### Objective
Verify that the system validates quantity and rejects zero quantity.

#### Preconditions
- User is authenticated

#### Test Steps
1. Send POST request to `/api/cart/add`
2. Provide quantity = 0:
   ```json
   {
     "productId": "<valid-product-id>",
     "quantity": 0
   }
   ```
3. Verify validation error

#### Expected Result
- **Status Code:** 400 Bad Request
- **Error Response:**
  ```json
  {
    "timestamp": "2024-01-15T10:30:00",
    "traceId": "<uuid>",
    "errorCode": "VALIDATION_ERROR",
    "message": "Validation failed",
    "details": [
      {
        "field": "quantity",
        "issue": "Quantity must be at least 1"
      }
    ]
  }
  ```

#### Assertions
- ✅ Status code is 400
- ✅ Error code is VALIDATION_ERROR
- ✅ Validation details include field name
- ✅ Validation message is clear

---

### TC006 - Add Item with Negative Quantity

**Test Case ID:** TC006  
**Priority:** High  
**Category:** Negative Test - Validation  
**Endpoint:** POST /api/cart/add

#### Objective
Verify that the system rejects negative quantity values.

#### Preconditions
- User is authenticated

#### Test Steps
1. Send POST request to `/api/cart/add`
2. Provide negative quantity:
   ```json
   {
     "productId": "<valid-product-id>",
     "quantity": -5
   }
   ```
3. Verify validation error

#### Expected Result
- **Status Code:** 400 Bad Request
- Error code: VALIDATION_ERROR
- Validation message indicates quantity must be positive

#### Assertions
- ✅ Status code is 400
- ✅ Error code is VALIDATION_ERROR
- ✅ Appropriate validation message

---

### TC007 - Add Item Exceeding Stock

**Test Case ID:** TC007  
**Priority:** High  
**Category:** Negative Test - Business Logic  
**Endpoint:** POST /api/cart/add

#### Objective
Verify that the system prevents adding quantity exceeding available stock.

#### Preconditions
- User is authenticated
- Product has limited stock (e.g., 100 units)

#### Test Steps
1. Send POST request to `/api/cart/add`
2. Provide quantity exceeding stock:
   ```json
   {
     "productId": "<valid-product-id>",
     "quantity": 1000
   }
   ```
3. Verify out-of-stock error

#### Expected Result
- **Status Code:** 409 Conflict
- **Error Response:**
  ```json
  {
    "timestamp": "2024-01-15T10:30:00",
    "traceId": "<uuid>",
    "errorCode": "OUT_OF_STOCK",
    "message": "Product out of stock"
  }
  ```

#### Assertions
- ✅ Status code is 409
- ✅ Error code is OUT_OF_STOCK
- ✅ Error message indicates stock issue

---

### TC008 - Add Item with Missing Product ID

**Test Case ID:** TC008  
**Priority:** Medium  
**Category:** Negative Test - Validation  
**Endpoint:** POST /api/cart/add

#### Objective
Verify that the system validates required fields and rejects requests with missing productId.

#### Preconditions
- User is authenticated

#### Test Steps
1. Send POST request to `/api/cart/add`
2. Provide request body without productId:
   ```json
   {
     "quantity": 2
   }
   ```
3. Verify validation error

#### Expected Result
- **Status Code:** 400 Bad Request
- Error code: VALIDATION_ERROR
- Validation details indicate productId is required

#### Assertions
- ✅ Status code is 400
- ✅ Error code is VALIDATION_ERROR
- ✅ Field validation message for productId

---

## Get Cart - Positive Tests

### TC009 - Get Cart with Items

**Test Case ID:** TC009  
**Priority:** High  
**Category:** Positive Test  
**Endpoint:** GET /api/cart

#### Objective
Verify that a user can retrieve their cart with all items and correct calculations.

#### Preconditions
- User is authenticated
- Cart exists with at least one item

#### Test Steps
1. Send GET request to `/api/cart`
2. Include authentication credentials
3. Verify response

#### Expected Result
- **Status Code:** 200 OK
- **Response Body:**
  ```json
  {
    "id": "<cart-id>",
    "userId": "testuser",
    "items": [
      {
        "id": "<item-id>",
        "productId": "<product-id>",
        "productName": "Wireless Mouse",
        "price": 49.99,
        "quantity": 2,
        "subtotal": 99.98
      }
    ],
    "totalPrice": 99.98,
    "itemCount": 1
  }
  ```

#### Assertions
- ✅ Status code is 200
- ✅ Cart ID is present
- ✅ UserId matches authenticated user
- ✅ Items array contains all cart items
- ✅ Each item has required fields: id, productId, productName, price, quantity, subtotal
- ✅ Subtotal = price × quantity for each item
- ✅ Total price = sum of all subtotals
- ✅ Item count matches number of items

---

## Update Item Quantity - Positive Tests

### TC010 - Update Item Quantity to Valid Value

**Test Case ID:** TC010  
**Priority:** High  
**Category:** Positive Test  
**Endpoint:** PUT /api/cart/{productId}

#### Objective
Verify that a user can update the quantity of an existing cart item.

#### Preconditions
- User is authenticated
- Product exists in cart
- New quantity is within stock limits

#### Test Steps
1. Send PUT request to `/api/cart/{productId}`
2. Provide new quantity:
   ```json
   {
     "quantity": 3
   }
   ```
3. Verify response

#### Expected Result
- **Status Code:** 200 OK
- Item quantity updated to 3
- Subtotal recalculated
- Total price recalculated

#### Assertions
- ✅ Status code is 200
- ✅ Item quantity updated correctly
- ✅ Subtotal = price × new quantity
- ✅ Total price recalculated

---

## Update Item Quantity - Negative Tests

### TC011 - Update with Zero Quantity

**Test Case ID:** TC011  
**Priority:** High  
**Category:** Negative Test - Validation  
**Endpoint:** PUT /api/cart/{productId}

#### Objective
Verify that the system rejects quantity update to zero.

#### Preconditions
- User is authenticated
- Product exists in cart

#### Test Steps
1. Send PUT request to `/api/cart/{productId}`
2. Provide quantity = 0:
   ```json
   {
     "quantity": 0
   }
   ```
3. Verify validation error

#### Expected Result
- **Status Code:** 400 Bad Request
- Error code: VALIDATION_ERROR
- Validation message indicates quantity must be at least 1

#### Assertions
- ✅ Status code is 400
- ✅ Error code is VALIDATION_ERROR
- ✅ Appropriate validation message

---

### TC012 - Update Non-Existent Product

**Test Case ID:** TC012  
**Priority:** Medium  
**Category:** Negative Test  
**Endpoint:** PUT /api/cart/{productId}

#### Objective
Verify that the system returns error when attempting to update a product not in cart.

#### Preconditions
- User is authenticated

#### Test Steps
1. Send PUT request to `/api/cart/non-existent-product`
2. Provide valid quantity:
   ```json
   {
     "quantity": 2
   }
   ```
3. Verify error response

#### Expected Result
- **Status Code:** 404 Not Found
- Error code: PRODUCT_NOT_FOUND
- Error message indicates product not found in cart

#### Assertions
- ✅ Status code is 404
- ✅ Error code is PRODUCT_NOT_FOUND
- ✅ Descriptive error message

---

### TC013 - Update Quantity Exceeding Stock

**Test Case ID:** TC013  
**Priority:** High  
**Category:** Negative Test - Business Logic  
**Endpoint:** PUT /api/cart/{productId}

#### Objective
Verify that the system prevents updating quantity beyond available stock.

#### Preconditions
- User is authenticated
- Product exists in cart
- Product has limited stock

#### Test Steps
1. Send PUT request to `/api/cart/{productId}`
2. Provide quantity exceeding stock:
   ```json
   {
     "quantity": 500
   }
   ```
3. Verify out-of-stock error

#### Expected Result
- **Status Code:** 409 Conflict
- Error code: OUT_OF_STOCK
- Error message indicates insufficient stock

#### Assertions
- ✅ Status code is 409
- ✅ Error code is OUT_OF_STOCK
- ✅ Error message about stock limitation

---

## Remove Item from Cart - Positive Tests

### TC014 - Remove Existing Item

**Test Case ID:** TC014  
**Priority:** High  
**Category:** Positive Test  
**Endpoint:** DELETE /api/cart/{productId}

#### Objective
Verify that a user can successfully remove an item from their cart.

#### Preconditions
- User is authenticated
- Product exists in cart

#### Test Steps
1. Send DELETE request to `/api/cart/{productId}`
2. Verify response

#### Expected Result
- **Status Code:** 200 OK
- Item removed from cart
- Items array no longer contains the product
- Total price recalculated
- Item count decremented

#### Assertions
- ✅ Status code is 200
- ✅ Item removed from items array
- ✅ Total price recalculated correctly
- ✅ Item count decremented

---

## Remove Item from Cart - Negative Tests

### TC015 - Remove Non-Existent Item

**Test Case ID:** TC015  
**Priority:** Medium  
**Category:** Negative Test  
**Endpoint:** DELETE /api/cart/{productId}

#### Objective
Verify that the system returns error when attempting to remove a product not in cart.

#### Preconditions
- User is authenticated

#### Test Steps
1. Send DELETE request to `/api/cart/non-existent-product`
2. Verify error response

#### Expected Result
- **Status Code:** 404 Not Found
- Error code: PRODUCT_NOT_FOUND
- Error message indicates product not found in cart

#### Assertions
- ✅ Status code is 404
- ✅ Error code is PRODUCT_NOT_FOUND
- ✅ Descriptive error message

---

## Authentication Tests

### TC016 - Access Cart Without Authentication

**Test Case ID:** TC016  
**Priority:** Critical  
**Category:** Security Test  
**Endpoint:** GET /api/cart

#### Objective
Verify that the system requires authentication for cart access.

#### Preconditions
- None (no authentication provided)

#### Test Steps
1. Send GET request to `/api/cart` without authentication
2. Verify unauthorized response

#### Expected Result
- **Status Code:** 401 Unauthorized
- Access denied

#### Assertions
- ✅ Status code is 401
- ✅ Access denied without authentication

---

### TC017 - Add Item Without Authentication

**Test Case ID:** TC017  
**Priority:** Critical  
**Category:** Security Test  
**Endpoint:** POST /api/cart/add

#### Objective
Verify that the system requires authentication for adding items to cart.

#### Preconditions
- None (no authentication provided)

#### Test Steps
1. Send POST request to `/api/cart/add` without authentication
2. Provide valid request body
3. Verify unauthorized response

#### Expected Result
- **Status Code:** 401 Unauthorized
- Request rejected

#### Assertions
- ✅ Status code is 401
- ✅ Cannot add items without authentication

---

## Edge Cases and Boundary Tests

### TC018 - Add Item with Maximum Allowed Quantity

**Test Case ID:** TC018  
**Priority:** Medium  
**Category:** Boundary Test  
**Endpoint:** POST /api/cart/add

#### Objective
Verify that the system accepts the maximum allowed quantity (100).

#### Preconditions
- User is authenticated
- Product has sufficient stock (≥100)

#### Test Steps
1. Send POST request to `/api/cart/add`
2. Provide quantity = 100:
   ```json
   {
     "productId": "<valid-product-id>",
     "quantity": 100
   }
   ```
3. Verify response

#### Expected Result
- **Status Code:** 200 OK (if stock available) or 409 Conflict (if insufficient stock)
- If successful, item added with quantity = 100

#### Assertions
- ✅ Status code is 200 or 409
- ✅ If 200, quantity is 100
- ✅ If 409, appropriate error message

---

### TC019 - Add Item Exceeding Maximum Quantity Limit

**Test Case ID:** TC019  
**Priority:** Medium  
**Category:** Boundary Test - Negative  
**Endpoint:** POST /api/cart/add

#### Objective
Verify that the system rejects quantity exceeding the maximum limit (>100).

#### Preconditions
- User is authenticated

#### Test Steps
1. Send POST request to `/api/cart/add`
2. Provide quantity = 101:
   ```json
   {
     "productId": "<valid-product-id>",
     "quantity": 101
   }
   ```
3. Verify validation error

#### Expected Result
- **Status Code:** 400 Bad Request
- Error code: VALIDATION_ERROR
- Validation message indicates quantity cannot exceed 100

#### Assertions
- ✅ Status code is 400
- ✅ Error code is VALIDATION_ERROR
- ✅ Validation message about max quantity

---

### TC020 - Get Empty Cart

**Test Case ID:** TC020  
**Priority:** Medium  
**Category:** Edge Case  
**Endpoint:** GET /api/cart

#### Objective
Verify that the system handles empty cart scenario correctly.

#### Preconditions
- User is authenticated
- User has no items in cart (new user or cleared cart)

#### Test Steps
1. Send GET request to `/api/cart`
2. Verify response

#### Expected Result
- **Status Code:** 200 OK or 404 Not Found
- If 200:
  ```json
  {
    "id": "<cart-id>",
    "userId": "testuser",
    "items": [],
    "totalPrice": 0,
    "itemCount": 0
  }
  ```
- If 404: Cart not found error

#### Assertions
- ✅ Status code is 200 or 404
- ✅ If 200, items array is empty
- ✅ If 200, totalPrice is 0
- ✅ If 200, itemCount is 0

---

## Test Execution Summary

### Test Coverage

| Category | Test Cases | Coverage |
|----------|------------|----------|
| Positive Tests | 5 | 25% |
| Negative Tests - Validation | 5 | 25% |
| Negative Tests - Business Logic | 3 | 15% |
| Security Tests | 2 | 10% |
| Edge Cases & Boundary Tests | 3 | 15% |
| Get/Retrieve Tests | 2 | 10% |
| **Total** | **20** | **100%** |

### API Endpoint Coverage

| Endpoint | Method | Test Cases | Status |
|----------|--------|------------|--------|
| /api/cart/add | POST | 9 | ✅ Complete |
| /api/cart | GET | 3 | ✅ Complete |
| /api/cart/{productId} | PUT | 4 | ✅ Complete |
| /api/cart/{productId} | DELETE | 2 | ✅ Complete |
| Authentication | ALL | 2 | ✅ Complete |

### Test Scenarios Covered

✅ **Happy Path Scenarios**
- Add single item to cart
- Add multiple quantities
- Increment existing item quantity
- Retrieve cart with items
- Update item quantity
- Remove item from cart

✅ **Validation Scenarios**
- Null/missing product ID
- Zero quantity
- Negative quantity
- Exceeding maximum quantity limit (>100)

✅ **Business Logic Scenarios**
- Product not found
- Out of stock
- Insufficient stock for requested quantity
- Product not in cart (for update/remove)

✅ **Security Scenarios**
- Unauthorized access to cart
- Unauthorized add to cart

✅ **Edge Cases**
- Maximum allowed quantity (100)
- Empty cart
- Boundary values

### Non-Functional Requirements Tested

✅ **Performance**
- Response time < 500ms for add to cart operation

✅ **Data Integrity**
- Price calculations (subtotal, total)
- Quantity increments
- Item count accuracy

✅ **Error Handling**
- Standardized error response format
- Appropriate HTTP status codes
- Descriptive error messages
- Trace IDs for debugging

### Test Execution Instructions

#### Using Postman
1. Import `test/postman/collection.json`
2. Import `test/postman/environment.json`
3. Update environment variables:
   - Set `productId1`, `productId2`, `productId3` with actual product IDs from database
4. Run collection with authentication
5. Review test results

#### Using Newman (CLI)
```bash
newman run test/postman/collection.json \
  -e test/postman/environment.json \
  --reporters cli,html \
  --reporter-html-export test/reports/newman-report.html
```

### Expected Test Results

| Status | Count | Percentage |
|--------|-------|------------|
| Pass | 20 | 100% |
| Fail | 0 | 0% |
| Skip | 0 | 0% |

### Known Limitations

1. **Product IDs:** Test environment variables need to be updated with actual product IDs from the database after application startup.
2. **Test Data Dependency:** Some tests depend on previous test execution (e.g., TC003 depends on TC001).
3. **Cleanup:** Tests do not automatically clean up cart data between runs.

### Recommendations

1. **Test Data Management:** Implement test data setup/teardown scripts
2. **Independent Tests:** Make tests independent by creating fresh test data for each test
3. **Automated Execution:** Integrate Newman into CI/CD pipeline
4. **Performance Testing:** Add load testing for concurrent cart operations
5. **Database State:** Consider using database snapshots for consistent test execution

---

## Appendix

### Error Code Reference

| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| PRODUCT_NOT_FOUND | 404 | Product does not exist or not found in cart |
| OUT_OF_STOCK | 409 | Requested quantity exceeds available stock |
| VALIDATION_ERROR | 400 | Request validation failed |
| CART_NOT_FOUND | 404 | Cart does not exist for user |
| CART_OPERATION_ERROR | 400 | General cart operation error |
| INTERNAL_ERROR | 500 | Unexpected server error |

### Sample cURL Commands

#### Add Item to Cart
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -u testuser:password \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "<product-id>",
    "quantity": 2
  }'
```

#### Get Cart
```bash
curl -X GET http://localhost:8080/api/cart \
  -u testuser:password
```

#### Update Item Quantity
```bash
curl -X PUT http://localhost:8080/api/cart/<product-id> \
  -u testuser:password \
  -H "Content-Type: application/json" \
  -d '{
    "quantity": 5
  }'
```

#### Remove Item from Cart
```bash
curl -X DELETE http://localhost:8080/api/cart/<product-id> \
  -u testuser:password
```

---

**Document Version:** 1.0  
**Last Updated:** 2024-01-15  
**Author:** QA Automation Agent  
**Status:** Ready for Execution
