# 🍔 FoodFlow – Online Food Delivery Backend

FoodFlow is a **scalable backend system** for an online food delivery platform (inspired by Zomato / Swiggy), built using **Spring Boot 4**, **Java 21**, **PostgreSQL**, **Redis**, and **Docker**.

The project is designed as a **modular monolith**, following clean architecture and best practices, with an easy path to microservices in the future.

---

## 🚀 Features Implemented

### 👤 User Module

* Create user
* Fetch user by ID
* Phone number validation (Indian format)
* Custom exception handling

### 🏪 Restaurant Module

* Create restaurant (with image upload)
* Get restaurant by ID
* Get restaurants by city
* Get all restaurants
* Redis caching for read-heavy APIs
* Optimized DB indexing for city, open status, rating

### 🍽 Menu Module

* Create menu item (with image upload)
* Get menu item by ID
* Get all menu items
* Get menu items by restaurant
* Menu items grouped by **category**
* Redis caching with proper eviction strategy

### 🖼 File Storage

* Local filesystem storage
* Docker-volume compatible
* Static image access via `/uploads/**`

### ⚡ Caching (Redis)

* Cache at **service layer** (best practice)
* TTL-based caching
* Cache eviction on create/update operations
* JSON serialization using Jackson

### 🧱 Cross-Cutting Concerns

* Global exception handling
* Centralized CORS configuration
* DTO-based API responses (no entity exposure)
* Clean separation between modules
* Query services to avoid repository coupling

---

## 🏗 Architecture

**Type:** Modular Monolith (Microservice-ready)

```
com.foodflow
 ├── user
 ├── restaurant
 ├── menu
 ├── filestorage
 ├── common
 │    ├── exceptions
 │    └── dto
 └── config
```

### Key Design Decisions

* No repository sharing across modules
* Communication via services + DTOs
* Redis used only for read-heavy endpoints
* DB indexes based on real query patterns
* Cache invalidation handled explicitly

---

## 🧑‍💻 Tech Stack

| Layer            | Technology                  |
| ---------------- | --------------------------- |
| Language         | Java 21                     |
| Framework        | Spring Boot 4               |
| Database         | PostgreSQL                  |
| Cache            | Redis                       |
| ORM              | Spring Data JPA (Hibernate) |
| Build Tool       | Maven                       |
| Containerization | Docker & Docker Compose     |
| File Storage     | Local FS (volume-mounted)   |

---

## 📦 Running the Project

### 🔹 Prerequisites

* Java 21
* Maven
* Docker & Docker Compose

### ▶️ Run Locally (Without Docker)

```bash
mvn clean spring-boot:run
```

Backend will start at:

```
http://localhost:8080
```

### 🐳 Run with Docker (Recommended)

```bash
docker compose up -d
```

Services started:

* Backend → `http://localhost:8080`
* PostgreSQL → `5432`
* Redis → `6379`

---

## 🖼 Image Access

Uploaded images are accessible directly:

```
http://localhost:8080/uploads/restaurant/<image-name>
http://localhost:8080/uploads/menuitem/<image-name>
```

Works both locally and inside Docker.

---

## 🧠 Caching Strategy

| API                     | Cache Name         |
| ----------------------- | ------------------ |
| Get all restaurants     | `allRestaurants`   |
| Get restaurants by city | `restaurantByCity` |
| Get restaurant by ID    | `restaurant`       |
| Get menu item by ID     | `menuItem`         |
| Get all menu items      | `allMenuItems`     |
| Get menu by restaurant  | `menuByRestaurant` |

Cache eviction happens automatically on:

* Restaurant creation
* Menu item creation

---

## 🗄 Database Optimization

### Restaurant Indexes

* `city`
* `city + is_open`
* `city + rating`

### Menu Item Indexes

* `restaurant_id`
* `category`
* `restaurant_id + category`

Designed for **high read throughput**.

---

## 🔐 CORS Configuration

Allowed origins:

* `http://localhost:1234`
* `http://localhost:3000`

Supports:

* Credentials
* Preflight requests
* Future JWT integration

---

## ⚠ Exception Handling

Centralized error handling using `@RestControllerAdvice`.

Example error response:

```json
{
  "timestamp": "2025-01-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Restaurant not found with id 10",
  "path": "/api/restaurant/10"
}
```

---

## 🔜 What’s Coming Next

* 🛒 Cart Module
* 📦 Order Module (end-to-end flow)
* 🔐 JWT Authentication & Authorization
* 📄 Pagination & Sorting
* 🧾 Order state machine
* ☁️ Cloud storage (S3-compatible)

---

## 👨‍💻 Author

**Kamlesh Yadav**
Backend Engineer | Java | Spring Boot | Distributed Systems

---

## ⭐ Final Note

This project intentionally focuses on **backend correctness, scalability, and architecture**, not shortcuts.

If you’re reviewing this project:

> Look at **design decisions**, not just features.
