# MyProject - Shopping Cart Application

## Overview
A Spring Boot 3.5.9 application implementing a shopping cart system with RESTful APIs. This project demonstrates modern Java development practices using JDK 21, Spring Boot, JPA, and comprehensive testing.

## Features
- ✅ Add items to shopping cart
- ✅ View cart contents
- ✅ Update item quantities
- ✅ Remove items from cart
- ✅ Real-time price calculation
- ✅ Stock validation
- ✅ JWT-ready authentication (demo mode enabled)
- ✅ CORS support for Angular frontend
- ✅ Comprehensive error handling
- ✅ H2 in-memory database
- ✅ JUnit 5 tests with JaCoCo coverage

## Technology Stack
- **Java**: JDK 21
- **Framework**: Spring Boot 3.5.9
- **Database**: H2 (in-memory)
- **Security**: Spring Security with JWT support
- **Testing**: JUnit 5, Mockito
- **Build Tool**: Maven
- **Code Coverage**: JaCoCo

## Project Structure
```
code/
├── src/
│   ├── main/
│   │   ├── java/com/myproject/
│   │   │   ├── controllers/        # REST Controllers
│   │   │   ├── services/           # Business Logic
│   │   │   │   ├── impl/          # Service Implementations
│   │   │   │   └── interfaces/    # Service Interfaces
│   │   │   ├── models/
│   │   │   │   ├── entities/      # JPA Entities
│   │   │   │   ├── dtos/          # Data Transfer Objects
│   │   │   │   └── repositories/  # JPA Repositories
│   │   │   ├── config/            # Configuration Classes
│   │   │   ├── exceptions/        # Custom Exceptions
│   │   │   └── MyprojectApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                      # Test Classes
└── pom.xml
```

## API Endpoints

### Cart Operations

#### 1. Add Item to Cart
```http
POST /api/cart/add
Content-Type: application/json

{
  "productId": "550e8400-e29b-41d4-a716-446655440000",
  "quantity": 2
}
```

**Response (201 Created):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "userId": "987e6543-e21b-12d3-a456-426614174999",
  "items": [
    {
      "id": "456e7890-e12b-34d5-a678-426614174111",
      "productId": "550e8400-e29b-41d4-a716-446655440000",
      "productName": "Wireless Mouse",
      "quantity": 2,
      "price": 49.99,
      "subtotal": 99.98
    }
  ],
  "totalPrice": 99.98,
  "itemCount": 1
}
```

#### 2. Get Cart
```http
GET /api/cart
```

**Response (200 OK):** Same as Add Item response

#### 3. Update Item Quantity
```http
PUT /api/cart/{productId}
Content-Type: application/json

{
  "quantity": 5
}
```

**Response (200 OK):** Updated cart response

#### 4. Remove Item from Cart
```http
DELETE /api/cart/{productId}
```

**Response (200 OK):** Updated cart response

## Error Responses

All errors follow a standard format:
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "traceId": "abc123-def456-ghi789",
  "errorCode": "PRODUCT_NOT_FOUND",
  "message": "Product not found",
  "details": []
}
```

### Error Codes
- `PRODUCT_NOT_FOUND` (404): Product does not exist
- `OUT_OF_STOCK` (409): Requested quantity exceeds available stock
- `CART_NOT_FOUND` (404): Cart not found for user
- `VALIDATION_ERROR` (400): Request validation failed
- `INTERNAL_SERVER_ERROR` (500): Unexpected error

## Getting Started

### Prerequisites
- JDK 21 or higher
- Maven 3.8+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/loginkarun/bank5.git
cd bank5
git checkout feature/SCRUM-11692-springboot-code
cd code
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### H2 Console
Access the H2 database console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:cartdb`
- Username: `sa`
- Password: (leave empty)

## Testing

### Run all tests:
```bash
mvn test
```

### Generate coverage report:
```bash
mvn clean verify
mvn jacoco:report
```

Coverage report will be available at: `target/site/jacoco/index.html`

## Sample Data

The application initializes with sample data:

**User:**
- ID: `987e6543-e21b-12d3-a456-426614174999`
- Username: `testuser`
- Email: `testuser@example.com`

**Products:**
1. Wireless Mouse - $49.99 (Stock: 100)
2. Mechanical Keyboard - $129.99 (Stock: 50)
3. USB-C Hub - $39.99 (Stock: 75)
4. Laptop Stand - $59.99 (Stock: 30)

## Configuration

### CORS
Configured to allow requests from `http://localhost:4200` (Angular default port)

### Security
Demo mode: All endpoints are accessible without authentication
Production: Uncomment JWT authentication in `SecurityConfig.java`

## GitHub Actions

The project includes a CI/CD workflow that:
- Builds the project with Maven
- Runs all tests
- Generates JaCoCo coverage reports
- Uploads test results and coverage artifacts

Trigger manually via GitHub Actions tab.

## API Testing with cURL

### Add Item to Cart
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "550e8400-e29b-41d4-a716-446655440000",
    "quantity": 2
  }'
```

### Get Cart
```bash
curl http://localhost:8080/api/cart
```

### Update Quantity
```bash
curl -X PUT http://localhost:8080/api/cart/550e8400-e29b-41d4-a716-446655440000 \
  -H "Content-Type: application/json" \
  -d '{"quantity": 5}'
```

### Remove Item
```bash
curl -X DELETE http://localhost:8080/api/cart/550e8400-e29b-41d4-a716-446655440000
```

## Development Notes

### Adding New Products
Products can be added via the `DataInitializer` class or through JPA repository methods.

### Database Schema
The application uses JPA with `ddl-auto=create-drop`, which recreates the schema on each restart.

### Logging
Logging is configured at INFO level for root and DEBUG for `com.myproject` package.

## Future Enhancements
- [ ] Implement full JWT authentication
- [ ] Add user registration and login
- [ ] Integrate with external payment gateway
- [ ] Add order history
- [ ] Implement product search and filtering
- [ ] Add inventory management
- [ ] Implement wishlist functionality

## License
This project is part of the bank5 repository.

## Contact
For issues or questions, please open an issue in the GitHub repository.

---

**Generated for SCRUM-11692: Add Items to Shopping Cart**
