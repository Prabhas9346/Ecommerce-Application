# E-Commerce Application

A comprehensive Spring Boot e-commerce platform with role-based access control, JWT authentication, and full CRUD operations for products, orders, and cart management.

## 🛍️ Features

- **User Authentication & Authorization**
  - JWT-based authentication with refresh tokens
  - Role-based access control (Admin, Seller, Consumer)
  - Secure registration and login system

- **Product Management**
  - Sellers can create, update, and delete products
  - Product filtering and search capabilities
  - Category-based product organization

- **Shopping Cart**
  - Add/remove items from cart
  - Update item quantities
  - Cart persistence across sessions

- **Order Management**
  - Checkout process with multiple payment methods
  - Order status tracking
  - Order history for consumers

- **Admin Dashboard**
  - Seller request approval/rejection
  - User management capabilities

## 🏗️ Architecture

### Technology Stack
- **Backend**: Spring Boot 3.3.5
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT
- **Documentation**: Swagger/OpenAPI 3
- **Build Tool**: Maven
- **Java Version**: 21

### Project Structure
```
src/main/java/com/prabhas/ecommerce/
├── controller/          # REST API endpoints
│   ├── AdminController.java
│   ├── ConsumerController.java
│   ├── PublicController.java
│   └── SellerController.java
├── service/             # Business logic layer
├── models/              # JPA entities
├── beans/               # DTOs and request objects
├── security/            # Security configuration
├── repositories/        # Data access layer
└── config/              # Application configuration
```

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- PostgreSQL database
- Maven 3.6 or higher

### Database Setup
1. Create a PostgreSQL database named `ecommerce`
2. Update database credentials in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   ```

### Running the Application
1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
4. The application will start on `http://localhost:8080`

## 📚 API Documentation

### Swagger UI
Access the interactive API documentation at:
```
http://localhost:8080/swagger-ui/index.html
```

### Key Endpoints

#### Public Endpoints
- `POST /api/public/register` - User registration
- `POST /api/public/login` - User login
- `POST /api/public/refresh` - Refresh JWT token
- `GET /api/public/get/products` - Get all products
- `GET /api/public/get/product/{id}` - Get product details

#### Consumer Endpoints
- `POST /api/auth/consumer/add-product/{id}` - Add product to cart
- `GET /api/auth/consumer/cart` - Get cart items
- `PUT /api/auth/consumer/cart/items/{productId}` - Update cart item
- `DELETE /api/auth/consumer/cart/items/{productId}` - Remove cart item
- `POST /api/auth/consumer/cart/checkout` - Checkout cart

#### Seller Endpoints
- `POST /api/auth/seller/add/product` - Create new product
- `GET /api/auth/seller/get/products` - Get seller's products
- `PUT /api/auth/seller/update/product/{id}` - Update product
- `DELETE /api/auth/seller/delete/product/{id}` - Delete product

#### Admin Endpoints
- `POST /api/auth/admin/approve-seller/{id}` - Approve seller request
- `POST /api/auth/admin/reject-seller/{id}` - Reject seller request

## 🔐 Security

### Authentication
- JWT tokens for authentication
- Refresh token mechanism for extended sessions
- Secure password storage with BCrypt

### Authorization
- Role-based access control
- Endpoint-level security using Spring Security
- Method-level security annotations

### User Roles
- **CONSUMER**: Can browse products, manage cart, place orders
- **SELLER**: Can manage products, view sales data
- **ADMIN**: Can approve seller requests, manage users

## 🗄️ Database Schema

### Core Entities
- **Users**: User accounts with roles and authentication
- **Products**: Product catalog with seller information
- **Category**: Product categorization
- **Cart**: Shopping cart for each consumer
- **CartItem**: Individual items in cart
- **Order**: Purchase orders
- **OrderItem**: Items within orders
- **Address**: User shipping addresses
- **PaymentMethod**: Available payment options

### Entity Relationships
- User ↔ Order (One-to-Many)
- User ↔ Cart (One-to-One)
- Cart ↔ CartItem (One-to-Many)
- Product ↔ CartItem (One-to-Many)
- Product ↔ OrderItem (One-to-Many)
- User ↔ Roles (Many-to-Many)

For detailed ER diagram, see `ER_DIAGRAM.md`.

## 🧪 Testing

### Running Tests
```bash
./mvnw test
```

### Test Coverage
- Unit tests for service layer
- Integration tests for controllers
- Security tests for authentication flows

## 🔧 Configuration

### Application Properties
Key configuration options in `application.properties`:
- Database connection settings
- JWT secret key
- Server port (default: 8080)
- JPA/Hibernate settings

### Environment Variables
- `SPRING_DATASOURCE_URL` - Database URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `JWT_SECRET` - JWT signing secret

## 📦 Dependencies

### Core Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Validation
- PostgreSQL Driver
- Lombok
- JWT (jjwt)
- ModelMapper
- Swagger/OpenAPI

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

[//]: # (## 📄 License)

[//]: # ()
[//]: # (This project is licensed under the MIT License.)

## 📞 Support

For any queries or issues, please raise an issue in the repository or contact +918309077535 | prabhas9346@gmail.com

---

**Note**: This is a demonstration project showcasing Spring Boot, Spring Security, and e-commerce functionality.
