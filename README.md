# 🍔 FoodFlow – Online Food Delivery Backend

FoodFlow is a **scalable backend system** for an online food delivery platform (inspired by Zomato / Swiggy), built using **Spring Boot 4**, **Java 21**, **PostgreSQL**, **Redis**, and **Docker**.

The project is designed as a **modular monolith**, following clean architecture and best practices, with a clear and safe path to microservices in the future.

---

## 🚀 Features Implemented

---

## 👤 User Module

* Create user
* Fetch user by ID
* Phone number validation (Indian format)
* Custom business exceptions
* Query service abstraction (no repository leakage)

---

## 🏪 Restaurant Module

* Create restaurant (with image upload)
* Get restaurant by ID
* Get restaurants by city
* Get all restaurants
* Redis caching for read-heavy APIs
* DB indexing for:

    * City
    * City + open status
    * City + rating

---

## 🍽 Menu Module

* Create menu item (with image upload)
* Get menu item by ID
* Get all menu items
* Get menu items by restaurant
* Menu items grouped by **category**
* Redis caching with proper eviction strategy
* Optimized composite indexes:

    * `restaurant_id`
    * `category`
    * `restaurant_id + category`

---

## 🛒 Cart Module

* Add item to cart
* Update cart item quantity
* Remove item from cart
* Get cart by user
* Clear cart
* Enforces **single-restaurant cart rule**
* Accurate total price & quantity calculation
* Clean separation of Cart & CartItem entities

---

## 📦 Order Module

### Order Flow

```
User → Cart → Checkout → Order
```

### Features

* Checkout from cart
* Order creation with snapshot of menu items
* List user orders with pagination
* Update order status (controlled lifecycle)
* Cancel order (rule-based)
* Order status validation using state machine
* Prevents illegal transitions
* Clean DTO responses (no entity exposure)

---

## 🔁 Order Lifecycle

```
CREATED
  ├── ACCEPTED
  │     ├── PREPARING
  │     │     ├── OUT_FOR_DELIVERY
  │     │     │     └── DELIVERED
  │     │
  │     └── CANCELLED
  │
  └── CANCELLED
```

* Lifecycle enforced via `OrderStatusValidator`
* Actor-based transitions (USER / RESTAURANT / SYSTEM)

---

## ⏱ Auto-Cancel Scheduler

* Background scheduler cancels stale orders
* Automatically cancels orders stuck in `CREATED`
* Runs periodically using Spring Scheduler
* System-initiated cancellation (safe & isolated)
* Prevents order buildup and stale data

---

## 🖼 File Storage

* Local filesystem storage
* Docker-volume compatible
* Static image access via `/uploads/**`
* Same behavior in local & Docker environments

```
/uploads/restaurant/*
/uploads/menuitem/*
```

---

## ⚡ Caching (Redis)

* Cache applied at **service layer**
* TTL-based caching
* Explicit cache eviction on write operations
* JSON serialization using Jackson
* Prevents redundant DB hits on hot APIs

### Cached APIs

| API                     | Cache Name         |
| ----------------------- | ------------------ |
| Get all restaurants     | `allRestaurants`   |
| Get restaurants by city | `restaurantByCity` |
| Get restaurant by ID    | `restaurant`       |
| Get menu item by ID     | `menuItem`         |
| Get all menu items      | `allMenuItems`     |
| Get menu by restaurant  | `menuByRestaurant` |

---

## 🧱 Cross-Cutting Concerns

* Global exception handling (`@RestControllerAdvice`)
* Centralized CORS configuration
* DTO-based API contracts
* Modular package structure
* Query services to avoid tight coupling
* Transaction-safe write operations

---

## 🏗 Architecture

**Type:** Modular Monolith (Microservice-ready)

```
com.foodflow
 ├── user
 ├── restaurant
 ├── menu
 ├── cart
 ├── order
 │    ├── scheduler
 │    ├── validator
 │    └── enums
 ├── filestorage
 ├── common
 │    ├── exceptions
 │    └── dto
 └── config
```

### Key Design Decisions

* No repository sharing across modules
* Communication via services + DTOs
* Redis used only for read-heavy paths
* DB indexes driven by real query patterns
* Cache invalidation handled explicitly
* Order lifecycle enforced at domain level

---

## 🧑‍💻 Tech Stack

| Layer        | Technology                  |
| ------------ | --------------------------- |
| Language     | Java 21                     |
| Framework    | Spring Boot 4               |
| Database     | PostgreSQL                  |
| Cache        | Redis                       |
| ORM          | Spring Data JPA (Hibernate) |
| Build Tool   | Maven                       |
| Containers   | Docker & Docker Compose     |
| File Storage | Local FS (volume-mounted)   |

---

## 📦 Running the Project

### 🔹 Prerequisites

* Java 21
* Maven
* Docker & Docker Compose

---

### ▶️ Run Locally (Without Docker)

```bash
mvn clean spring-boot:run
```

Application starts at:

```
http://localhost:8080
```

---

### 🐳 Run with Docker (Recommended)

```bash
docker compose up -d
```

Services started:

* Backend → `http://localhost:8080`
* PostgreSQL → `5432`
* Redis → `6379`

---

## 🔐 CORS Configuration

Allowed origins:

* `http://localhost:1234`
* `http://localhost:3000`

Supports:

* Credentials
* Preflight requests
* Easy JWT integration later

---

## ⚠ Exception Handling

Centralized error handling with consistent API responses.

Example:

```json
{
  "timestamp": "2025-01-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Order not found with id 10",
  "path": "/api/orders/10"
}
```

---

## 🔜 What’s Coming Next

* 💳 Payment & Refund flow
* 🔐 JWT Authentication & Authorization
* 📜 Order timeline / audit log
* 🚚 Delivery partner lifecycle
* 📡 Event-driven order updates (Kafka)
* ☁️ Cloud storage (S3 compatible)

---

## 👨‍💻 Author

**Kamlesh Yadav**
Backend Engineer | Java | Spring Boot | Distributed Systems

---

## ⭐ Final Note

This project focuses on **backend correctness, scalability, and clean architecture**.

If you’re reviewing this project:

> Look at **design decisions, domain modeling, and lifecycle control**, not just CRUD features.
