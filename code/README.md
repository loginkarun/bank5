# MyProject - Shopping Cart Application

## Overview
A SpringBoot-based shopping cart application that allows users to add products to their cart, manage quantities, and view cart totals.

## Technology Stack
- **Java**: 21
- **Spring Boot**: 3.5.9
- **Database**: H2 (in-memory)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito
- **Code Coverage**: JaCoCo

## Features
- Add items to cart
- Update item quantities
- Remove items from cart
- View cart with total price
- Product stock validation
- JWT-based authentication (Basic Auth for demo)
- CORS enabled for Angular frontend

## API Endpoints

### Cart Operations
- **POST** `/api/cart/add` - Add item to cart
- **GET** `/api/cart` - Get current cart
- **DELETE** `/api/cart/{productId}` - Remove item from cart
- **PUT** `/api/cart/{productId}` - Update item quantity

## Running the Application

### Prerequisites
- JDK 21
- Maven 3.6+

### Build and Run
```bash
cd code
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

### Default Test User
- Username: `testuser`
- Password: `password`

### H2 Console
Access H2 console at: `http://localhost:8080/api/h2-console`
- JDBC URL: `jdbc:h2:mem:cartdb`
- Username: `sa`
- Password: (leave empty)

## Testing

### Run Tests
```bash
cd code
mvn test
```

### Generate Coverage Report
```bash
cd code
mvn jacoco:report
```
Report available at: `code/target/site/jacoco/index.html`

## Project Structure
```
code/
├── src/
│   ├── main/
│   │   ├── java/com/myproject/
│   │   │   ├── controllers/       # REST controllers
│   │   │   ├── models/
│   │   │   │   ├── dtos/         # Data Transfer Objects
│   │   │   │   ├── entities/     # JPA entities
│   │   │   │   └── repositories/ # JPA repositories
│   │   │   ├── services/
│   │   │   │   ├── impl/         # Service implementations
│   │   │   │   └── interfaces/   # Service interfaces
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── exceptions/       # Custom exceptions
│   │   │   └── MyprojectApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                     # Test classes
└── pom.xml
```

## Sample Products
The application initializes with the following products:
1. Wireless Mouse - $49.99 (Stock: 100)
2. Mechanical Keyboard - $129.99 (Stock: 50)
3. USB-C Cable - $19.99 (Stock: 200)
4. Laptop Stand - $79.99 (Stock: 75)
5. Webcam HD - $89.99 (Stock: 30)

## CORS Configuration
CORS is enabled for Angular frontend at `http://localhost:4200`

## Error Handling
The application provides structured error responses with:
- Timestamp
- Trace ID
- Error code
- Error message
- Field-level validation details

## Security
- Basic Authentication (for demo)
- Session management
- CSRF protection disabled for API
- H2 console accessible without authentication

## Future Enhancements
- JWT token-based authentication
- Database persistence (PostgreSQL/MySQL)
- Product catalog management
- Order processing
- Payment integration

## License
MIT License