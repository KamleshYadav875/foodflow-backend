# 🍔 FoodFlow – Online Food Delivery Backend

FoodFlow is a **production-grade backend system** for an online food delivery platform inspired by **Swiggy / Zomato**, built using **Spring Boot**, **Java**, **PostgreSQL**, **Redis**, **JWT**, **OAuth2**, **Docker**, and **Razorpay**.

The project is intentionally designed as a **modular monolith** with strong domain boundaries, clean separation of concerns, and real-world workflows — making it **microservice-ready** without premature complexity.

---

## 🚀 Features Overview

---

## 🔐 Authentication & Security

### Authentication
- JWT-based authentication (stateless)
- Phone + password login
- Google OAuth2 login
- Secure password hashing using BCrypt

### Authorization
- Role-based access control using `@PreAuthorize`
- Roles:
  - `USER`
  - `RESTAURANT`
  - `DELIVERY`
- Method-level security enabled

### Security Highlights
- Custom JWT authentication filter
- OAuth2 success handler with auto user provisioning
- Stateless session management
- Centralized `SecurityConfig`

---

## 👤 User Module

- User registration with **Indian phone number validation**
- Login with JWT
- User profile with:
  - Total orders
  - Active orders
  - Cancelled orders
- View own orders (paginated)
- Cancel own orders (rule-based)
- Clean separation of **Command** and **Query** services

---

## 🏪 Restaurant Module

- Restaurant onboarding with multipart image upload
- Automatic **restaurant admin creation**
- Get restaurant by ID
- Get all restaurants
- Get restaurants by city
- Redis caching for read-heavy APIs
- Optimized queries using DB indexes
- DTO-based responses (no entity leakage)

---

## 🍽 Menu Module

- Create menu items (multipart image upload)
- Get menu item by ID
- Get all menu items
- Get menu items by restaurant
- Menu grouping by **category**
- Availability validation
- Cache eviction on write operations

---

## 🛒 Cart Module

- Add item to cart
- Update item quantity
- Remove item
- Clear cart
- Enforces **single-restaurant cart rule**
- Automatic total & quantity recalculation
- Fully transactional and concurrency-safe

---

## 📦 Order Module

- Checkout from cart
- Immutable order snapshot creation
- Order lifecycle management:
  - CREATED
  - PLACED
  - ACCEPTED
  - PREPARING
  - READY
  - OUT_FOR_PICKUP
  - PICKED_UP
  - DELIVERED
  - CANCELLED / REJECTED
- User order listing (paginated)
- Restaurant order listing (paginated)
- Order detail view
- Auto-cancel unpaid orders (scheduler-based)

---

## 🚚 Delivery Module

- Register as delivery partner
- Delivery partner profile with statistics
- Availability management:
  - ONLINE
  - OFFLINE
  - BUSY
- City-based order broadcast strategy
- Delivery assignment workflow:
  - PENDING → ACCEPTED → COMPLETED
- Current delivery tracking
- Delivery history
- Automatic partner availability updates

> Delivery assignment is designed using the **Strategy Pattern** for future enhancements  
> (distance-based, load-based, etc.).

---

## 💳 Payment Module (Razorpay)

- Payment created after checkout
- Razorpay hosted payment link generation
- Secure webhook handling
- Payment lifecycle:
  - PENDING → SUCCESS
- Order status updated **only after payment success**
- Fully backend-driven payment flow

---

## 🖼 File Storage

- Local filesystem storage
- Docker-volume compatible
- Static resource access via Spring MVC


  ```
  /uploads/restaurant/**
  /uploads/menuitem/**
  ```

---

### ⚡ Caching (Redis)

* Service-layer caching (best practice)
* TTL-based cache strategy
* Cache eviction on writes
* Redis used for **read-heavy APIs**

Cached data includes:
- Restaurants
- Restaurants by city
- Menu items
- Restaurant menus

---

### 🧱 Cross-Cutting Concerns

- Global exception handling (`@RestControllerAdvice`)
- Centralized API error response model
- Custom domain exceptions
- Centralized CORS configuration
- DTO mapping using ModelMapper
- Scheduler support
- Clean modular package structure

---

## 🏗 Architecture Overview
<img width="1536" height="1024" alt="Architecture Diagram" src="https://github.com/user-attachments/assets/45c9d2e7-836a-4745-8356-4a64d1fb377b" />


**Type:** Modular Monolith (Microservice-ready)


```
com.foodflow
 ├── auth
 ├── user
 ├── restaurant
 ├── menu
 ├── cart
 ├── order
 ├── delivery
 ├── payment
 ├── filestorage
 ├── security
 ├── common
 │    ├── exceptions
 │    └── dto
 └── config
```

### Key Design Principles

* No repository sharing across modules
* Command vs Query service separation
* Domain-driven structure
* Explicit transaction boundaries
* Backend-first flow (frontend optional)

---

## 🧑‍💻 Tech Stack

| Layer            | Technology                  |
|------------------|-----------------------------|
| Language         | Java                        |
| Framework        | Spring Boot                 |
| Security         | Spring, JWT, OAuth2         |
| ORM              | Spring Data JPA (Hibernate) |
| Database         | PostgreSQL                  |
| Cache            | Redis                       |
| Payments         | Razorpay                    |
| Build Tool       | Maven                       |
| Containerization | Docker & Docker Compose     |
| File Storage     | Local FS (Volume Mounted)   |

---

## ⚙ Configuration (application.yml)

Key configurations:

* PostgreSQL datasource
* Redis cache
* Multipart upload limits
* File storage location
* Razorpay credentials
* OAuth2 client configuration
* JWT secret & expiration
* Scheduler settings
* Actuator metrics

---

## ▶ Running the Project

### 🔹 Prerequisites

* Java
* Maven
* Docker & Docker Compose

---

### ▶ Run Locally

```bash
mvn clean spring-boot:run
```

Server runs at:

```
http://localhost:8080
```

---

### 🐳 Run with Docker

```bash
docker compose up -d
```

Services started:

* Backend → `http://localhost:8080`
* PostgreSQL → `5432`
* Redis → `6379`

---

## 🔔 Payment Flow (Razorpay)

1. User checks out → Order created
2. Payment record created (PENDING)
3. Razorpay payment link generated
4. User completes payment
5. Razorpay calls webhook
6. Backend verifies & updates:

  * Payment → SUCCESS
  * Order → PLACED

---

## 🧠 Notable Design Decisions

* Orders are immutable snapshots
* Payment success drives order placement
* Delivery assignment is asynchronous & decoupled
* No tight coupling between modules
* Easily extensible to microservices

---

## 🔜 Future Enhancements

* OTP-based authentication
* Payment failure & refund handling
* Kafka-based async events
* Real-time notifications
* Distance-based delivery assignment
* Cloud file storage (S3-compatible)
* Kubernetes deployment

---

## 👨‍💻 Author

**Kamlesh Yadav**
Backend Engineer | Java | Spring Boot | Distributed Systems

---

## ⭐ Final Note

This project focuses on **real-world backend engineering**:

* Correctness over shortcuts
* Architecture over hacks
* Scalability over demos

If you’re reviewing this project:

> Look at **design choices, boundaries, and flows** — not just endpoints.
