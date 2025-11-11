# Transport V2.0 - AI-Powered Delivery Route Optimization System

## 🚀 Project Overview
This is an advanced Spring Boot application for managing and optimizing delivery routes using **three intelligent algorithms**:
- **Nearest Neighbor Algorithm**: Fast heuristic optimization
- **Clarke-Wright Algorithm**: Advanced savings-based optimization
- **AI Optimizer (NEW V2.0)**: Machine learning-powered optimization using historical delivery patterns

## 🆕 What's New in V2.0
- ✨ **Customer Management System**: New Customer entity for better address management
- 📊 **Delivery History Tracking**: Automated history collection for AI analysis
- 🤖 **AI-Powered Optimization**: Spring AI integration with Ollama/LLM support
- 🗄️ **Liquibase Migration**: Professional database version control
- ⚙️ **Multi-Environment YAML Config**: Separate dev (in-memory H2) and qa (file-based H2) environments
- 📄 **Pagination & Advanced Search**: Handle large datasets efficiently
- 🎯 **Annotation-Based DI**: Replaced XML configuration with modern annotations
- 🧪 **Integration Tests**: Comprehensive testing with JUnit 5
- 📊 **JaCoCo Code Coverage**: Quality metrics and reporting

## 🛠️ Technologies Used
- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA** with pagination support
- **Spring AI** with Ollama integration
- **Liquibase** for database migrations
- **H2 Database** (in-memory for dev, file-based for qa)
- **Lombok** for boilerplate reduction
- **Swagger/OpenAPI** for API documentation
- **JUnit 5** with integration tests
- **JaCoCo** for code coverage
- **SLF4J** for logging

## 🎯 Key Features

### Core Features (V1 + V2)
- **CRUD Operations**: Warehouses, Vehicles, Deliveries, Tours, **Customers (NEW)**, **Delivery History (NEW)**
- **Three Optimization Algorithms**: Nearest Neighbor, Clarke-Wright, **AI-Powered (NEW)**
- **Customer Management**: Centralized customer data with address, preferences, and contact info
- **Historical Analysis**: Automatic delivery history creation when tours complete
- **Vehicle Constraints**: Weight and volume capacity validation
- **Delivery Status Tracking**: PENDING, IN_TRANSIT, DELIVERED, FAILED
- **Tour Status Management (NEW)**: PENDING, IN_PROGRESS, COMPLETED, CANCELLED
- **GPS Distance Calculation**: Haversine formula for accurate routing
- **RESTful API**: Complete REST interface with proper HTTP methods
- **Pagination**: Handle large datasets efficiently
- **Advanced Search**: Multi-criteria searching with fuzzy matching

### AI & Intelligence
- **Pattern Recognition**: Analyzes delivery history by day of week, time slot, and location
- **Delay Prediction**: Learns from historical delays to avoid problem time windows
- **Smart Recommendations**: AI provides justifications for routing decisions
- **Confidence Scoring**: Each AI recommendation includes a confidence score
- **Fallback Mechanism**: Gracefully degrades to nearest neighbor if AI unavailable

### Database & Migration
- **Liquibase Integration**: Full database version control
- **Multi-Environment Support**: Dev (in-memory H2) and QA (file-based H2)
- **Automatic Migration**: V1 to V2 data migration included
- **Rollback Capability**: Safe database rollback for any migration

## Vehicle Types and Constraints
- **BIKE**: Max 15kg, 0.5m³
- **VAN**: Max 800kg, 10m³
- **TRUCK**: Max 3500kg, 40m³

## Project Structure
```
src/
├── main/
│   ├── java/org/example/transport/
│   │   ├── controller/       # REST Controllers
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── entity/           # JPA Entities
│   │   ├── enums/            # Enumerations
│   │   ├── exception/        # Custom Exceptions
│   │   ├── mapper/           # DTO Mappers
│   │   ├── optimizer/        # Tour Optimization Algorithms
│   │   ├── repository/       # JPA Repositories
│   │   ├── service/          # Business Logic
│   │   └── util/             # Utility Classes
│   └── resources/
│       ├── application.properties
│       └── applicationContext.xml
└── test/                     # Unit Tests
```

## 🚀 Running the Application

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- **Ollama** (optional, for AI optimizer) - [Install Ollama](https://ollama.ai)

### Quick Start (Development Environment)
```bash
# 1. Build the project
mvn clean install

# 2. Run with dev profile (uses H2 in-memory database)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Or specify explicitly
java -jar target/Transport-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

The application will start on `http://localhost:8080`

### Running with AI Optimizer

**Option 1: Using Ollama (Recommended for Development)**
```bash
# 1. Install and start Ollama
ollama serve

# 2. Pull TinyLlama model (lightweight, perfect for dev)
ollama pull tinyllama

# 3. Run application with AI optimizer enabled
mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dtour.optimizer.algorithm=AI
```

**Option 2: Using Cloud LLM**
Configure your LLM API key in environment variables and update `application-dev.yml`

### Running QA Environment (File-based H2)
```bash
# Run application with qa profile (uses file-based H2 for persistent data)
mvn spring-boot:run -Dspring-boot.run.profiles=qa

# Access H2 Console: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:file:./data/transport_qa
# Username: sa
# Password: (leave empty)
```

### Environment Variables
Create a `.env` file in the project root:
```env
# Server
SERVER_PORT=8080

# Ollama (for AI Optimizer)
OLLAMA_BASE_URL=http://localhost:11434
OLLAMA_MODEL=tinyllama

# Optimizer Selection
OPTIMIZER_ALGORITHM=NEAREST_NEIGHBOR  # Options: NEAREST_NEIGHBOR, CLARKE_WRIGHT, AI

# Logging
LOG_LEVEL_APP=INFO
LOG_LEVEL_WEB=INFO
LOG_LEVEL_SQL=DEBUG
```

## 📚 API Documentation
Once the application is running, access:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **H2 Console** (dev): http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:transportdb_dev`
  - Username: `sa`
  - Password: (leave empty)

## 📡 Main API Endpoints

### 🆕 V2.0 - Customers
- `GET /api/customers` - Get all customers
- `GET /api/customers/paginated?page=0&size=10` - Get customers with pagination
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Deactivate customer
- `GET /api/customers/active` - Get active customers only
- `GET /api/customers/search?query=Paris&page=0&size=10` - Search customers

### V1.0 - Warehouses

### Warehouses
- `GET /api/warehouses` - Get all warehouses
- `GET /api/warehouses/{id}` - Get warehouse by ID
- `POST /api/warehouses` - Create warehouse
- `PUT /api/warehouses/{id}` - Update warehouse
- `DELETE /api/warehouses/{id}` - Delete warehouse

### Vehicles
- `GET /api/vehicles` - Get all vehicles
- `GET /api/vehicles/{id}` - Get vehicle by ID
- `POST /api/vehicles` - Create vehicle
- `PUT /api/vehicles/{id}` - Update vehicle
- `DELETE /api/vehicles/{id}` - Delete vehicle
- `GET /api/vehicles/available` - Get available vehicles
- `GET /api/vehicles/type/{type}` - Get vehicles by type

### Deliveries
- `GET /api/deliveries` - Get all deliveries
- `GET /api/deliveries/{id}` - Get delivery by ID
- `POST /api/deliveries` - Create delivery
- `PUT /api/deliveries/{id}` - Update delivery
- `DELETE /api/deliveries/{id}` - Delete delivery
- `GET /api/deliveries/status/{status}` - Get deliveries by status
- `GET /api/deliveries/unassigned` - Get unassigned deliveries
- `PATCH /api/deliveries/{id}/status?status={status}` - Update delivery status

### Tours
- `GET /api/tours` - Get all tours
- `GET /api/tours/{id}` - Get tour by ID
- `POST /api/tours` - Create tour
- `DELETE /api/tours/{id}` - Delete tour
- `POST /api/tours/{tourId}/deliveries/{deliveryId}` - Add delivery to tour
- `GET /api/tours/{id}/optimize?algorithm={NEAREST_NEIGHBOR|CLARKE_WRIGHT}` - Optimize tour
- `GET /api/tours/{id}/distance` - Get total distance
- `GET /api/tours/date/{date}` - Get tours by date
- `GET /api/tours/statistics/average-distance?algorithm={algorithm}` - Get average distance by algorithm

## 💡 Example Usage - V2.0 Features

### 1. Create a Customer (V2.0)
```json
POST /api/customers
{
  "name": "Acme Corporation",
  "address": "45 Avenue des Champs-Élysées, Paris",
  "latitude": 48.8698,
  "longitude": 2.3078,
  "preferredTimeSlot": "14:00-16:00",
  "phone": "+33142563456",
  "email": "contact@acme.fr"
}
```

### 2. Create a Delivery for Customer (V2.0)
```json
POST /api/deliveries
{
  "customerId": 1,
  "weightKg": 35.0,
  "volumeM3": 2.5,
  "preferredTimeSlot": "14:00-16:00"
}
```
Note: The delivery uses the customer's address by default. You can override with `specificAddress`, `specificLatitude`, `specificLongitude` if needed.

### 3. Create a Warehouse
```json
POST /api/warehouses
{
  "name": "Main Depot",
  "address": "123 Warehouse St, Paris",
  "latitude": 48.8566,
  "longitude": 2.3522,
  "openingTime": "06:00",
  "closingTime": "22:00"
}
```

### 2. Create a Vehicle
```json
POST /api/vehicles
{
  "registrationNumber": "VAN-001",
  "type": "VAN"
}
```

### 3. Create Deliveries
```json
POST /api/deliveries
{
  "address": "10 Rue de Rivoli, Paris",
  "latitude": 48.8606,
  "longitude": 2.3376,
  "weightKg": 25.5,
  "volumeM3": 1.2,
  "preferredTimeSlot": "09:00-11:00"
}
```

### 4. Create a Tour
```json
POST /api/tours
{
  "tourDate": "2024-10-27",
  "vehicleId": 1,
  "warehouseId": 1
}
```

### 5. Add Deliveries to Tour
```
POST /api/tours/1/deliveries/1
POST /api/tours/1/deliveries/2
POST /api/tours/1/deliveries/3
```

### 6. Optimize the Tour with AI (V2.0)
```bash
# Traditional optimization
GET /api/tours/1/optimize?algorithm=CLARKE_WRIGHT

# AI-powered optimization (requires Ollama running)
GET /api/tours/1/optimize?algorithm=AI
```

The AI optimizer will:
1. Analyze historical delivery data for the same day of week
2. Consider customer preferred time slots
3. Learn from past delays and traffic patterns
4. Provide recommendations and confidence scores
5. Fallback to nearest neighbor if AI is unavailable

### 7. Complete a Tour and Generate History (V2.0)
```json
PATCH /api/tours/1/status
{
  "status": "COMPLETED"
}
```
This automatically creates DeliveryHistory records for AI analysis!

## Design Patterns Used
- **Repository Pattern**: Data access abstraction
- **DTO Pattern**: Separation of domain and presentation layers
- **Mapper Pattern**: Converting between entities and DTOs
- **Strategy Pattern**: Different optimization algorithms (TourOptimizer interface)
- **Dependency Injection**: XML-based configuration (Open/Closed Principle)

## XML-Based Dependency Injection
The project uses XML-based dependency injection via `applicationContext.xml` to demonstrate:
- **Open/Closed Principle**: Application is closed for modification but open for extension
- Easy switching between different optimizer implementations
- No use of `@Autowired`, `@Service`, `@Component` annotations for business logic

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run with Code Coverage (JaCoCo)
```bash
mvn clean test jacoco:report
```
View coverage report: `target/site/jacoco/index.html`

### Run Integration Tests Only
```bash
mvn test -Dtest=*IntegrationTest
```

### Test Categories
- **Unit Tests**: Service layer with Mockito mocks
- **Integration Tests**: Full API testing with TestRestTemplate (V2.0)
- **Optimizer Tests**: Algorithm verification
- **Utility Tests**: Distance calculations

## 🗄️ Database Management with Liquibase

### View Liquibase Status
```bash
mvn liquibase:status
```

### Update Database to Latest Version
```bash
mvn liquibase:update
```

### Rollback Last Changeset
```bash
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

### Rollback to Specific Date
```bash
mvn liquibase:rollback -Dliquibase.rollbackDate=2025-11-03
```

### Generate Diff Between Databases
```bash
mvn liquibase:diff
```

### Liquibase Changelogs
Located in `src/main/resources/db/changelog/`:
- `db.changelog-master.xml` - Master changelog
- `db.changelog-v1.0-initial.xml` - V1.0 schema
- `db.changelog-v2.0-new-entities.xml` - V2.0 new entities (Customer, DeliveryHistory)
- `db.changelog-v2.0-data-migration.xml` - Migration script from V1 to V2

## Logging
The application uses SLF4J for logging. Log levels can be configured in `application.properties`.

## 📝 Important Notes

### V2.0 Changes
- **Database Migration**: Liquibase automatically migrates V1 data to V2 structure
- **Customer-Centric Model**: Deliveries now reference customers instead of storing addresses directly
- **AI Requirement**: AI optimizer requires Ollama or cloud LLM to be configured
- **Multi-Environment**: Dev uses in-memory H2, QA uses file-based H2 for persistence
- **Annotation-Based DI**: V2.0 uses `@Service`, `@Autowired` instead of XML configuration

### General Notes
- H2 database (dev) resets on application restart (in-memory)
- H2 database (qa) persists data to `./data/transport_qa.mv.db` file
- Distance calculations use Haversine formula
- Vehicle capacity constraints are strictly enforced
- Tour status must be set to COMPLETED manually to generate delivery history
- AI optimizer gracefully falls back to nearest neighbor if unavailable

## 🏗️ Architecture Highlights

### Design Patterns (V2.0)
- **Repository Pattern**: JPA repositories with custom queries
- **DTO Pattern**: Clean separation of API and domain models
- **Mapper Pattern**: Entity-DTO conversions
- **Strategy Pattern**: Pluggable optimization algorithms
- **Builder Pattern**: DeliveryHistory construction
- **Conditional Beans**: `@ConditionalOnProperty` for optimizer selection

### Key Technologies
- **Spring Boot 3.5.7**: Latest Spring framework
- **Spring AI**: AI integration framework
- **Liquibase**: Database version control
- **JPA/Hibernate**: ORM with pagination
- **Lombok**: Boilerplate reduction
- **JUnit 5 + Mockito**: Testing framework

## 🎯 Future Enhancements
- Real-time GPS tracking integration
- Mobile app for delivery drivers
- Email/SMS notifications for customers
- Machine learning model training on delivery history
- Route visualization on maps
- Weather API integration for route adjustment
- Multi-depot support

## 📊 Version History

### V2.0 (2025-11-04) - AI & Intelligence
- ✨ Added Customer entity and management
- ✨ Added DeliveryHistory for pattern analysis  
- ✨ Implemented AI-powered optimizer with Spring AI
- ✨ Integrated Liquibase for database migrations
- ✨ Multi-environment YAML configuration
- ✨ Pagination and advanced search
- ✨ Integration testing
- ✨ JaCoCo code coverage
- ✨ Replaced XML DI with annotations

### V1.0 (2024-10-27) - Core System
- Initial release with basic CRUD operations
- Two optimization algorithms (Nearest Neighbor, Clarke-Wright)
- Vehicle and warehouse management
- Basic delivery routing

## 👨‍💻 Author
Transport V2.0 - AI-Powered Delivery Route Optimization
Java 17 & Spring Boot 3.5.7 Project

## 📸 Screenshots

### Jira Board
![img_1.png](img_1.png)

### API Testing
![img.png](img.png)
