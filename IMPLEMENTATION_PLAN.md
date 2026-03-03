# Auto Insurance Quotation System — Implementation Plan
## Customer: Farmers Insurance (Bristol West)
## Project: quotegenerationapp

---

## Context
Bristol West (Farmers Insurance) needs a web-based auto insurance quotation system that allows customers to get real-time premium quotes online. The system collects customer, vehicle, driver, and coverage information, applies rating rules, and generates a full quote with premium breakdown. Built as a microservices application using Spring Boot + React.

---

## Architecture Overview

```
[React Frontend :3000]
         │
         ▼
[API Gateway :8080]              ← Spring Cloud Gateway + CORS
         │
         ├──► [Customer Service    :8081]  ← MySQL: bristolwest_customer
         ├──► [Vehicle Service     :8082]  ← MySQL: bristolwest_vehicle
         ├──► [Driver Service      :8083]  ← MySQL: bristolwest_driver
         ├──► [Rating Service      :8084]  ← MySQL: bristolwest_rating (stored proc)
         ├──► [Quote Service       :8085]  ← MySQL: bristolwest_quote (orchestrator)
         └──► [Notification Svc   :8086]  ← Kafka Consumer → Email

[Eureka Server :8761]            ← Service Discovery
[Kafka :9092]                    ← Async messaging (quote-generated topic)
[MySQL :3306]                    ← Per-service schemas
```

---

## 1. Project Directory Structure

```
quotegenerationapp/
├── pom.xml                                   ← Parent POM (all modules)
├── docker-compose.yml                        ← MySQL + Kafka + Zookeeper
├── IMPLEMENTATION_PLAN.md
├── .gitignore
│
├── eureka-server/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/bristolwest/eureka/
│       │   └── EurekaServerApplication.java
│       └── resources/application.yml
│
├── api-gateway/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/bristolwest/gateway/
│       │   ├── ApiGatewayApplication.java
│       │   └── config/
│       │       ├── GatewayConfig.java        ← Route definitions
│       │       └── CorsConfig.java
│       └── resources/application.yml
│
├── customer-service/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/bristolwest/customer/
│       │   ├── CustomerServiceApplication.java
│       │   ├── controller/CustomerController.java
│       │   ├── service/CustomerService.java
│       │   ├── service/CustomerServiceImpl.java
│       │   ├── repository/CustomerRepository.java
│       │   ├── entity/Customer.java
│       │   ├── dto/CustomerRequestDTO.java
│       │   ├── dto/CustomerResponseDTO.java
│       │   └── exception/GlobalExceptionHandler.java
│       ├── main/resources/
│       │   ├── application.yml
│       │   └── db/migration/V1__create_customers.sql
│       └── test/java/com/bristolwest/customer/CustomerServiceTest.java
│
├── vehicle-service/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/bristolwest/vehicle/
│       │   ├── VehicleServiceApplication.java
│       │   ├── controller/VehicleController.java
│       │   ├── service/VehicleService.java
│       │   ├── service/VehicleServiceImpl.java
│       │   ├── repository/VehicleRepository.java
│       │   ├── entity/Vehicle.java
│       │   ├── dto/VehicleRequestDTO.java
│       │   └── dto/VehicleResponseDTO.java
│       ├── main/resources/
│       │   ├── application.yml
│       │   └── db/migration/V1__create_vehicles.sql
│       └── test/java/com/bristolwest/vehicle/VehicleServiceTest.java
│
├── driver-service/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/bristolwest/driver/
│       │   ├── DriverServiceApplication.java
│       │   ├── controller/DriverController.java
│       │   ├── service/DriverService.java
│       │   ├── service/DriverServiceImpl.java
│       │   ├── repository/DriverRepository.java
│       │   ├── entity/Driver.java
│       │   ├── dto/DriverRequestDTO.java
│       │   └── dto/DriverResponseDTO.java
│       ├── main/resources/
│       │   ├── application.yml
│       │   └── db/migration/V1__create_drivers.sql
│       └── test/java/com/bristolwest/driver/DriverServiceTest.java
│
├── rating-service/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/bristolwest/rating/
│       │   ├── RatingServiceApplication.java
│       │   ├── controller/RatingController.java
│       │   ├── service/RatingService.java
│       │   ├── service/RatingServiceImpl.java
│       │   ├── repository/RatingFactorRepository.java
│       │   ├── entity/RatingFactor.java
│       │   ├── dto/RatingRequestDTO.java
│       │   └── dto/RatingResponseDTO.java
│       ├── main/resources/
│       │   ├── application.yml
│       │   └── db/migration/V1__create_rating.sql   ← includes stored proc
│       └── test/java/com/bristolwest/rating/RatingServiceTest.java
│
├── quote-service/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/bristolwest/quote/
│       │   ├── QuoteServiceApplication.java
│       │   ├── controller/QuoteController.java
│       │   ├── service/QuoteService.java
│       │   ├── service/QuoteServiceImpl.java
│       │   ├── repository/QuoteRepository.java
│       │   ├── entity/Quote.java
│       │   ├── entity/QuoteCoverage.java
│       │   ├── dto/QuoteRequestDTO.java
│       │   ├── dto/QuoteResponseDTO.java
│       │   ├── client/CustomerServiceClient.java    ← Feign
│       │   ├── client/VehicleServiceClient.java     ← Feign
│       │   ├── client/DriverServiceClient.java      ← Feign
│       │   ├── client/RatingServiceClient.java      ← Feign
│       │   └── kafka/QuoteEventProducer.java        ← Kafka producer
│       ├── main/resources/
│       │   ├── application.yml
│       │   └── db/migration/V1__create_quotes.sql
│       └── test/java/com/bristolwest/quote/QuoteServiceTest.java
│
├── notification-service/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/bristolwest/notification/
│       │   ├── NotificationServiceApplication.java
│       │   ├── kafka/QuoteEventConsumer.java        ← Kafka consumer
│       │   ├── service/EmailNotificationService.java
│       │   └── dto/QuoteGeneratedEvent.java
│       └── main/resources/application.yml
│
└── frontend/
    ├── package.json
    ├── public/index.html
    └── src/
        ├── App.js
        ├── index.js
        ├── components/
        │   ├── QuoteWizard.js          ← Multi-step form container
        │   ├── steps/
        │   │   ├── CustomerStep.js     ← Step 1: Customer Info
        │   │   ├── VehicleStep.js      ← Step 2: Vehicle Info
        │   │   ├── DriverStep.js       ← Step 3: Driver Info
        │   │   ├── CoverageStep.js     ← Step 4: Coverage Selection
        │   │   └── QuoteSummary.js     ← Step 5: Quote Result
        │   └── common/
        │       ├── Header.js
        │       ├── ProgressBar.js
        │       └── FormField.js
        ├── services/
        │   └── quoteService.js         ← Axios API calls to API Gateway
        └── styles/
            └── App.css
```

---

## 2. MySQL Database Schema

### customers (bristolwest_customer DB)
```sql
CREATE TABLE customers (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  first_name    VARCHAR(100) NOT NULL,
  last_name     VARCHAR(100) NOT NULL,
  email         VARCHAR(150) NOT NULL UNIQUE,
  phone         VARCHAR(20),
  date_of_birth DATE NOT NULL,
  address       VARCHAR(255),
  city          VARCHAR(100),
  state         CHAR(2),
  zip_code      VARCHAR(10),
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### vehicles (bristolwest_vehicle DB)
```sql
CREATE TABLE vehicles (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_id    BIGINT NOT NULL,
  vin            VARCHAR(17) UNIQUE,
  make           VARCHAR(100) NOT NULL,
  model          VARCHAR(100) NOT NULL,
  year           INT NOT NULL,
  usage_type     ENUM('PERSONAL','BUSINESS','COMMUTE') DEFAULT 'PERSONAL',
  annual_mileage INT,
  garaging_zip   VARCHAR(10),
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### drivers (bristolwest_driver DB)
```sql
CREATE TABLE drivers (
  id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_id        BIGINT NOT NULL,
  first_name         VARCHAR(100),
  last_name          VARCHAR(100),
  date_of_birth      DATE,
  license_number     VARCHAR(50),
  license_state      CHAR(2),
  years_licensed     INT,
  violations_count   INT DEFAULT 0,
  accidents_count    INT DEFAULT 0,
  created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### quotes (bristolwest_quote DB)
```sql
CREATE TABLE quotes (
  id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
  quote_number          VARCHAR(20) NOT NULL UNIQUE,   -- e.g. BW-2024-001234
  customer_id           BIGINT NOT NULL,
  vehicle_id            BIGINT NOT NULL,
  driver_id             BIGINT NOT NULL,
  coverage_type         ENUM('LIABILITY','STANDARD','FULL') NOT NULL,
  base_premium          DECIMAL(10,2),
  liability_premium     DECIMAL(10,2),
  collision_premium     DECIMAL(10,2),
  comprehensive_premium DECIMAL(10,2),
  total_premium         DECIMAL(10,2) NOT NULL,
  status                ENUM('ACTIVE','EXPIRED','CONVERTED') DEFAULT 'ACTIVE',
  created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  expires_at            TIMESTAMP
);
```

### rating_factors (bristolwest_rating DB)
```sql
CREATE TABLE rating_factors (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  factor_name  VARCHAR(100),
  factor_key   VARCHAR(100),
  factor_value DECIMAL(5,3),
  description  VARCHAR(255)
);
```

### MySQL Stored Procedure (rating-service)
```sql
DELIMITER //
CREATE PROCEDURE calculate_premium(
  IN  p_driver_age            INT,
  IN  p_violations            INT,
  IN  p_accidents             INT,
  IN  p_vehicle_year          INT,
  IN  p_annual_mileage        INT,
  IN  p_coverage_type         VARCHAR(20),
  IN  p_zip_code              VARCHAR(10),
  OUT p_base_premium          DECIMAL(10,2),
  OUT p_liability_premium     DECIMAL(10,2),
  OUT p_collision_premium     DECIMAL(10,2),
  OUT p_comprehensive_premium DECIMAL(10,2),
  OUT p_total_premium         DECIMAL(10,2)
)
BEGIN
  SET p_base_premium = 800.00;

  -- Age surcharge
  IF p_driver_age < 25 THEN
    SET p_base_premium = p_base_premium * 1.40;
  ELSEIF p_driver_age > 70 THEN
    SET p_base_premium = p_base_premium * 1.20;
  END IF;

  -- Violations & accidents
  SET p_base_premium = p_base_premium + (p_violations * 150.00);
  SET p_base_premium = p_base_premium + (p_accidents  * 200.00);

  -- Older vehicle discount
  IF p_vehicle_year < (YEAR(CURDATE()) - 10) THEN
    SET p_base_premium = p_base_premium * 0.90;
  END IF;

  -- High mileage surcharge
  IF p_annual_mileage > 15000 THEN
    SET p_base_premium = p_base_premium * 1.10;
  END IF;

  -- Coverage breakdown
  SET p_liability_premium     = p_base_premium * 0.40;
  SET p_collision_premium     = 0.00;
  SET p_comprehensive_premium = 0.00;

  IF p_coverage_type = 'STANDARD' THEN
    SET p_collision_premium = p_base_premium * 0.35;
  ELSEIF p_coverage_type = 'FULL' THEN
    SET p_collision_premium     = p_base_premium * 0.35;
    SET p_comprehensive_premium = p_base_premium * 0.25;
  END IF;

  SET p_total_premium = p_liability_premium + p_collision_premium + p_comprehensive_premium;
END //
DELIMITER ;
```

---

## 3. REST API Endpoints

| Service              | Method | Endpoint                          | Description              |
|----------------------|--------|-----------------------------------|--------------------------|
| **Customer** :8081   | POST   | /api/customers                    | Create customer          |
|                      | GET    | /api/customers/{id}               | Get by ID                |
|                      | PUT    | /api/customers/{id}               | Update customer          |
| **Vehicle** :8082    | POST   | /api/vehicles                     | Add vehicle              |
|                      | GET    | /api/vehicles/{id}                | Get by ID                |
|                      | GET    | /api/vehicles/customer/{cid}      | Get by customer          |
| **Driver** :8083     | POST   | /api/drivers                      | Add driver               |
|                      | GET    | /api/drivers/{id}                 | Get by ID                |
|                      | GET    | /api/drivers/customer/{cid}       | Get by customer          |
| **Rating** :8084     | POST   | /api/rating/calculate             | Calculate premium        |
| **Quote** :8085      | POST   | /api/quotes                       | Generate quote           |
|                      | GET    | /api/quotes/{id}                  | Get by ID                |
|                      | GET    | /api/quotes/number/{num}          | Get by quote number      |
|                      | GET    | /api/quotes/customer/{cid}        | Get customer's quotes    |
|                      | PUT    | /api/quotes/{id}/status           | Update status            |

All endpoints are accessible via API Gateway at `localhost:8080`.

---

## 4. Kafka Design

| Item         | Value                                                   |
|--------------|---------------------------------------------------------|
| Topic        | `quote-generated`                                       |
| Producer     | Quote Service (after persisting quote)                  |
| Consumer     | Notification Service (sends email)                      |
| Group ID     | `notification-group`                                    |

**QuoteGeneratedEvent payload:**
```json
{
  "quoteId": 1001,
  "quoteNumber": "BW-2026-001234",
  "customerEmail": "john.doe@email.com",
  "customerName": "John Doe",
  "totalPremium": 1245.50,
  "coverageType": "FULL",
  "vehicleMakeModel": "2022 Toyota Camry",
  "quoteExpiresAt": "2026-04-01"
}
```

---

## 5. Quote Generation Flow

```
POST /api/quotes  (QuoteRequestDTO — contains all 5 wizard step data)
      │
      ├─ 1. CustomerServiceClient.createCustomer()    → Feign → :8081
      ├─ 2. VehicleServiceClient.createVehicle()      → Feign → :8082
      ├─ 3. DriverServiceClient.createDriver()        → Feign → :8083
      ├─ 4. RatingServiceClient.calculatePremium()    → Feign → :8084
      ├─ 5. Generate unique quote number: BW-YYYY-XXXXXX
      ├─ 6. Persist Quote → MySQL (bristolwest_quote)
      ├─ 7. QuoteEventProducer.publish() → Kafka topic: quote-generated
      └─ 8. Return QuoteResponseDTO → API Gateway → React frontend
```

---

## 6. Spring Boot Dependencies Per Service

| Dependency                              | All | Gateway | Eureka | Quote | Notification |
|-----------------------------------------|-----|---------|--------|-------|--------------|
| spring-boot-starter-web                 |  ✓  |         |        |  ✓    |              |
| spring-boot-starter-data-jpa            |  ✓  |         |        |  ✓    |              |
| mysql-connector-java                    |  ✓  |         |        |  ✓    |              |
| spring-cloud-starter-eureka-client      |  ✓  |    ✓    |        |  ✓    |      ✓       |
| spring-cloud-starter-gateway            |     |    ✓    |        |       |              |
| spring-cloud-starter-netflix-eureka-server |  |        |   ✓    |       |              |
| spring-cloud-starter-openfeign          |     |         |        |  ✓    |              |
| spring-kafka                            |     |         |        |  ✓    |      ✓       |
| spring-boot-starter-mail                |     |         |        |       |      ✓       |
| spring-boot-starter-validation          |  ✓  |         |        |  ✓    |              |
| lombok                                  |  ✓  |         |        |  ✓    |      ✓       |
| spring-boot-starter-test + junit-jupiter|  ✓  |         |        |  ✓    |              |

---

## 7. Frontend — React Multi-Step Wizard

| Step | Component         | Fields                                                                 |
|------|-------------------|------------------------------------------------------------------------|
| 1    | CustomerStep.js   | First name, Last name, DOB, Email, Phone, Address, City, State, ZIP   |
| 2    | VehicleStep.js    | Year, Make, Model, VIN, Usage type, Annual mileage, Garaging ZIP      |
| 3    | DriverStep.js     | License #, License state, Years licensed, Violations, Accidents       |
| 4    | CoverageStep.js   | Radio: Liability / Standard (+Collision) / Full Coverage              |
| 5    | QuoteSummary.js   | Premium breakdown, Quote #, Expiry date, Email sent confirmation      |

- State: `useState` with shared `formData` object across steps
- API: `quoteService.js` → `POST http://localhost:8080/api/quotes`
- Branding: Bristol West blue/white color scheme

---

## 8. Infrastructure (docker-compose.yml)

```yaml
services:
  mysql:
    image: mysql:8.0
    ports: ["3306:3306"]
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: bristolwest

  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    ports: ["2181:2181"]

  kafka:
    image: confluentinc/cp-kafka:7.5.0
    ports: ["9092:9092"]
    depends_on: [zookeeper]
```

---

## 9. Testing Strategy (JUnit 5 + Mockito)

| Test                    | Type        | Tools                          |
|-------------------------|-------------|--------------------------------|
| CustomerServiceTest     | Unit        | Mockito, JUnit 5               |
| VehicleServiceTest      | Unit        | Mockito, JUnit 5               |
| DriverServiceTest       | Unit        | Mockito, JUnit 5               |
| RatingServiceTest       | Unit        | Mockito, MySQL stored proc     |
| QuoteServiceTest        | Unit        | Mockito (mock all Feign + Kafka)|
| CustomerControllerTest  | Integration | @SpringBootTest + MockMvc      |
| QuoteControllerTest     | Integration | @SpringBootTest + MockMvc      |

---

## 10. Implementation Order

```
Step 1 → docker-compose.yml              (infrastructure)
Step 2 → eureka-server                   (service registry)
Step 3 → pom.xml (parent)               (build setup)
Step 4 → customer-service               (leaf service)
Step 5 → vehicle-service                (leaf service)
Step 6 → driver-service                 (leaf service)
Step 7 → rating-service + stored proc   (rating engine)
Step 8 → quote-service                  (orchestrator)
Step 9 → notification-service           (Kafka consumer)
Step 10 → api-gateway                   (routing layer)
Step 11 → frontend (React)              (UI wizard)
```

---

## 11. Port Reference

| Service              | Port |
|----------------------|------|
| Eureka Server        | 8761 |
| API Gateway          | 8080 |
| Customer Service     | 8081 |
| Vehicle Service      | 8082 |
| Driver Service       | 8083 |
| Rating Service       | 8084 |
| Quote Service        | 8085 |
| Notification Service | 8086 |
| React Frontend       | 3000 |
| MySQL                | 3306 |
| Kafka                | 9092 |

---

## 12. Verification Checklist

- [ ] `docker-compose up` → MySQL, Kafka, Zookeeper running
- [ ] Eureka dashboard → `http://localhost:8761` → all services registered
- [ ] `POST http://localhost:8080/api/quotes` via Postman → 201 Created + quote JSON
- [ ] MySQL: quote row present in `bristolwest_quote.quotes` table
- [ ] Kafka: notification-service logs show event consumed + email sent
- [ ] React: `npm start` → 5-step wizard completes → quote summary displayed
- [ ] `mvn test` in each service → all JUnit tests pass
