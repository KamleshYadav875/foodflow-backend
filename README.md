# 🍔 FoodFlow – Online Food Delivery Backend

FoodFlow is a **production-grade backend system** for an online food delivery platform (inspired by **Zomato / Swiggy**), built using **Spring Boot**, **Java**, **PostgreSQL**, **Redis**, **Docker**, and **Razorpay**.

The project is intentionally designed as a **modular monolith** with clean separation of concerns, strong domain boundaries, and scalability in mind — making it easy to evolve into microservices later.

---

## 🚀 Core Features Implemented

### 👤 User Module

* User registration with phone validation (Indian format)
* User profile with order statistics
* View own orders (paginated)
* Cancel order (rule-based)
* Clean separation using **Command / Query services**

---

### 🏪 Restaurant Module

* Create restaurant (multipart image upload)
* Get restaurant by ID
* Get all restaurants
* Get restaurants by city
* Optimized queries with DB indexes
* Owner information mapping (DTO-based)

---

### 🍽 Menu Module

* Create menu items (with image upload)
* Get menu item by ID
* Get all menu items
* Get menu items by restaurant
* Menu grouped by **category**
* Availability checks before adding to cart

---

### 🛒 Cart Module

* Add item to cart
* Update item quantity
* Remove item from cart
* Clear cart
* Enforces **single-restaurant cart rule**
* Auto recalculation of totals
* Transaction-safe updates

---

### 📦 Order Module

* Checkout from cart
* Order item snapshot creation
* Paginated order listing:

  * User orders
  * Restaurant orders
* Order detail view (items + restaurant)
* Order lifecycle management
* Auto-cancel unpaid orders (scheduler)

---

### 🚚 Delivery Module

* Register as delivery partner
* Partner profile with stats
* Availability management
* Order broadcast strategy (city-based)
* Accept delivery assignment
* Current delivery tracking
* Delivery history
* Delivery status updates:

  * PICKED_UP
  * DELIVERED
* Automatic partner availability updates

> Designed using **Strategy Pattern** for future enhancements (distance-based, load-based assignment).

---

### 💳 Payment Module (Razorpay)

* Payment created after checkout
* Razorpay **Hosted Payment Link**
* Webhook-based payment confirmation
* Payment lifecycle:

  * PENDING → SUCCESS
* Order status updated **only after successful payment**
* Fully backend-driven (minimal frontend dependency)

---

### 🖼 File Storage

* Local filesystem storage
* Docker-volume compatible
* Static access via:

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

Cached APIs include:

* Restaurants
* Menu items
* Restaurant menus

---

### 🧱 Cross-Cutting Concerns

* Global exception handling (`@RestControllerAdvice`)
* Custom domain exceptions
* Centralized CORS configuration
* DTO-based API responses (no entity leakage)
* Transaction management
* Clean modular package structure

---

## 🏗 Architecture Overview

**Type:** Modular Monolith (Microservice-ready)

```
com.foodflow
 ├── user
 ├── restaurant
 ├── menu
 ├── cart
 ├── order
 ├── delivery
 ├── payment
 ├── filestorage
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
| ---------------- | --------------------------- |
| Language         | Java                        |
| Framework        | Spring Boot                 |
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
* Multipart uploads
* File storage location
* Razorpay credentials
* Auto-cancel order scheduler

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

## 🔐 Authentication (Current)

* Header-based user identification:

  ```
  X-USER-ID: <userId>
  ```

> JWT authentication is planned as a future enhancement.

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

* Payment failure handling
* Refund flow
* JWT Authentication & Authorization
* Real-time notifications (Kafka / WebSocket)
* Distance-based delivery assignment
* Cloud file storage (S3-compatible)

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
