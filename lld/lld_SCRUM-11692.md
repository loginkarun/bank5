# Low-Level Design Document (LLD)

## Objective
The objective of this requirement is to enable customers to add items to their shopping cart, so that they can purchase them later. The cart should reflect real-time updates for item quantity, price, and total cost, and persist across page navigation and browser refresh. The system must handle edge cases like adding the same item multiple times and ensure accurate cart updates.

## 1. JIRA Issue Metadata
- **Issue Key:** SCRUM-11692
- **Issue Title:** As a customer, I want to add items to my shopping cart, so that I can purchase them later.
- **Issue Description:**
  - Given a product is available, when the user clicks 'Add to Cart,' then the item should appear in the shopping cart with the correct quantity and price.
  - If the user adds the same item multiple times, the quantity of the item in the cart should increase accordingly.
  - The cart should display the updated total price after adding an item.
  - The cart should persist the added items even if the user navigates to another page or refreshes the browser.
  - Summary: This feature allows users to add items to their shopping cart from the product listing or product details page. The cart should reflect the added items in real-time, including their quantity, price, and total cost. The system should handle edge cases like adding the same item multiple times and ensure the cart is updated accordingly.
  - Business Logic: When a user clicks 'Add to Cart,' the system should check the product's availability and stock. If valid, the item should be added to the cart with a default quantity of 1. If the item already exists in the cart, the quantity should increment by 1. The cart total should be recalculated by summing the price of all items multiplied by their quantities.
- **Acceptance Criteria:**
  - Item is added to cart with correct quantity and price.
  - Adding the same item increases its quantity.
  - Cart displays updated total price.
  - Cart persists items across navigation and refresh.
- **Functional Requirements:**
  - Add item to cart
  - Increment item quantity if already present
  - Display cart contents with quantity, price, and total
  - Persist cart state
- **Validations:**
  - Product exists in database before adding
  - Product is in stock before adding
  - Cart updates correctly when same item is added multiple times
- **Non-Functional Requirements:**
  - Handle up to 10,000 concurrent users
  - Add to cart operation < 500ms
  - Cart available 99.9% uptime
  - Secure session tokens for access

---

## 2. SpringBoot Backend Details

### 2.1. Controller Layer
#### 2.1.1. REST API Endpoints
| Operation           | REST Method | URL             | Request Body                     | Response Body                      |
|---------------------|-------------|-----------------|----------------------------------|-------------------------------------|
| Add Item to Cart    | POST        | /api/cart/add   | { productId, quantity }          | { cartId, items, totalPrice }       |
| Get Cart            | GET         | /api/cart       | -                                | { cartId, items, totalPrice }       |
| Remove Item         | DELETE      | /api/cart/item  | { productId }                    | { cartId, items, totalPrice }       |
| Update Item Qty     | PUT         | /api/cart/item  | { productId, quantity }          | { cartId, items, totalPrice }       |

#### 2.1.2. Controller Classes
| Class Name         | Responsibility                          | Methods                      |
|--------------------|-----------------------------------------|------------------------------|
| CartController     | Handles cart operations (add, get, etc.)| addItem, getCart, removeItem, updateItemQuantity |

#### 2.1.3. Exception Handlers
- `@ControllerAdvice` class `GlobalExceptionHandler` for handling:
  - ProductNotFoundException
  - OutOfStockException
  - CartNotFoundException
  - ValidationException

### 2.2. Service Layer
#### 2.2.1. Business Logic Implementation
- `CartService` implements:
  - Add item to cart (check product existence & stock)
  - Increment item quantity
  - Calculate total price
  - Persist cart state

#### 2.2.2. Service Layer Architecture
- `CartService` interface
- `CartServiceImpl` implementation
- Uses `ProductService` for product validation

#### 2.2.3. Dependency Injection Configuration
- `@Service` for service classes
- `@Autowired` for repositories/services

#### 2.2.4. Validation Rules
| Field Name     | Validation                | Error Message                  | Annotation Used     |
|---------------|---------------------------|-------------------------------|---------------------|
| productId     | Exists in DB              | Product not found             | Custom validator    |
| quantity      | > 0, <= stock             | Invalid quantity/Out of stock | @Min, custom        |
| cartId        | Exists                    | Cart not found                | Custom validator    |

### 2.3. Repository/Data Access Layer
#### 2.3.1. Entity Models
| Entity     | Fields                                      | Constraints                |
|-----------|----------------------------------------------|----------------------------|
| Cart      | cartId, userId, items[], totalPrice          | cartId unique, userId FK   |
| CartItem  | itemId, productId, quantity, price           | productId FK, quantity > 0 |
| Product   | productId, name, price, stock                | productId unique, stock >= 0|

#### 2.3.2. Repository Interfaces
- `CartRepository extends JpaRepository<Cart, String>`
- `CartItemRepository extends JpaRepository<CartItem, String>`
- `ProductRepository extends JpaRepository<Product, String>`

#### 2.3.3. Custom Queries
- Find cart by userId
- Find product by productId

### 2.4. Configuration
#### 2.4.1. Application Properties
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.jpa.hibernate.ddl-auto=update`
- `server.port=8080`

#### 2.4.2. Spring Configuration Classes
- `CartConfig` for bean definitions if needed

#### 2.4.3. Bean Definitions
- Beans for services, repositories

### 2.5. Security
- JWT token-based authentication
- Authorization: Only authenticated users can access cart APIs
- Secure session tokens for cart persistence

### 2.6. Error Handling & Exceptions
- `GlobalExceptionHandler` for mapping exceptions to HTTP status codes
- Custom exceptions: ProductNotFoundException, OutOfStockException, CartNotFoundException
- HTTP Status codes: 404 (Not Found), 400 (Bad Request), 401 (Unauthorized), 500 (Internal Server Error)

---

## 3. Database Details
### 3.1. ER Model
```mermaid
erDiagram
    USER ||--o{ CART : owns
    CART ||--o{ CARTITEM : contains
    CARTITEM }o--|| PRODUCT : references
```

### 3.2. Table Schema
| Table Name | Columns                       | Data Types      | Constraints           |
|------------|-------------------------------|-----------------|-----------------------|
| Cart       | cart_id, user_id, total_price | VARCHAR, VARCHAR, DECIMAL | PK(cart_id), FK(user_id) |
| CartItem   | item_id, cart_id, product_id, quantity, price | VARCHAR, VARCHAR, VARCHAR, INT, DECIMAL | PK(item_id), FK(cart_id), FK(product_id) |
| Product    | product_id, name, price, stock| VARCHAR, VARCHAR, DECIMAL, INT | PK(product_id), stock >= 0 |

### 3.3. Database Validations
- product_id exists in Product table
- stock > 0 for adding to cart
- cart_id exists for cart operations

---

## 4. Non-Functional Requirements
### 4.1. Performance Considerations
- Add to cart operation < 500ms
- Support up to 10,000 concurrent users

### 4.2. Security Requirements
- Secure session tokens
- JWT authentication
- HTTPS for all API endpoints

### 4.3. Logging & Monitoring
- Log cart operations (add, remove, update)
- Monitor API response times
- Alert on cart service failures

---

## 5. Dependencies (Maven)
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-validation
- jjwt (JWT token handling)
- h2/postgresql/mysql (database)

---

## 6. Assumptions
- Cart is persisted in relational DB (not NoSQL)
- User authentication is handled by JWT
- Cart is associated with user session/account
- Product catalog is already available in Product table
- Cart APIs are only accessible to authenticated users

---

## LLD Files
- lld/lld_SCRUM-11692.md
