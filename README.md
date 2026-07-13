# Movie Ticket Booking System

## Overview

This project is a scalable backend application for an online movie ticket booking platform built using **Java 21** and **Spring Boot 3.5.16**. The primary objective was to design a production-oriented backend capable of handling seat-level concurrency, configurable pricing and refund rules, asynchronous event processing, and AI-powered movie recommendations.

The system supports multiple cities, multiple theatres within each city, multiple screens per theatre, and multiple shows for each screen. Customers can browse movies, reserve seats through a time-bound hold mechanism, complete payments, cancel bookings according to configurable refund policies, and receive asynchronous notifications.

To make the project more than a traditional CRUD application, an AI-powered recommendation engine has been integrated using **Google Gemini** and **Pinecone Vector Database**. Movie metadata is converted into semantic embeddings, indexed into Pinecone, and retrieved through similarity search to generate personalized recommendations using Retrieval-Augmented Generation (RAG).

The project intentionally follows clean architectural principles, strong separation of concerns, and extensible design patterns to resemble how a backend service would be structured in a production environment.

---

# Objectives

The primary goals while designing this system were:

* Design a scalable RESTful backend application.
* Prevent double-booking under concurrent seat selection.
* Separate business rules from infrastructure concerns.
* Demonstrate event-driven backend design.
* Keep the architecture modular and extensible.
* Integrate modern AI capabilities without affecting the core booking workflow.
* Maintain readability, testability, and maintainability.

---

# Key Features

## Customer Features

* User Registration & Login using JWT Authentication
* Browse Movies
* Browse Cities
* Browse Theatres
* Browse Shows
* View Available Seats
* Seat Hold with Automatic Expiration
* Booking Confirmation
* Booking Cancellation
* Booking History
* Payment Processing (Simulated)
* AI-Based Movie Recommendations

---

## Admin Features

* Manage Cities
* Manage Theatres
* Manage Screens
* Manage Seats
* Manage Movies
* Manage Shows
* Configure Pricing Rules
* Configure Discount Codes
* Configure Refund Policies
* View Bookings
* Manage Users

---

# Technical Highlights

The implementation focuses on backend engineering concepts commonly found in large-scale systems.

Major areas include:

* Layered Architecture
* REST API Design
* Spring Security with JWT
* PostgreSQL Persistence
* Hibernate/JPA
* Optimistic Locking
* Time-bound Seat Holds
* Event-Driven Processing
* Asynchronous Notifications
* Configurable Pricing Strategies
* Configurable Refund Strategies
* Validation
* Exception Handling
* AI Recommendation Engine

---

# Technology Stack

| Layer           | Technology                  |
| --------------- | --------------------------- |
| Language        | Java 21                     |
| Framework       | Spring Boot 3.5.16          |
| Security        | Spring Security + JWT       |
| Database        | PostgreSQL                  |
| ORM             | Spring Data JPA / Hibernate |
| Documentation   | OpenAPI / Swagger           |
| Build Tool      | Maven                       |
| AI Model        | Google Gemini               |
| Vector Database | Pinecone                    |
| IDE             | IntelliJ IDEA               |
| Testing         | JUnit 5, Mockito            |
| Version Control | Git & GitHub                |

---

# High-Level Architecture

The application follows a traditional layered architecture where each layer has a single responsibility.

```text
                REST APIs
                     │
                     ▼
              Controller Layer
                     │
                     ▼
               Service Layer
                     │
      ┌──────────────┼──────────────┐
      ▼              ▼              ▼
 Pricing       Booking Flow     AI Engine
                     │
                     ▼
             Repository Layer
                     │
                     ▼
                PostgreSQL

                     │
                     ▼
             Spring Events
                     │
                     ▼
          Async Notification Layer

                     │
                     ▼
          Gemini + Pinecone (AI)
```

Business logic remains inside the Service layer while Controllers are intentionally kept thin. Persistence concerns are isolated within repositories and all external integrations are encapsulated behind dedicated service abstractions.

---

# AI Recommendation Engine

One of the major additions to this assignment is an AI-powered recommendation engine built using **Google Gemini** and **Pinecone**.

Instead of relying on keyword matching, the recommendation system uses semantic search.

The workflow is:

```text
Movie
    │
    ▼
Generate Embedding (Gemini)
    │
    ▼
Store Vector (Pinecone)
    │
    ▼
Customer Booking History
    │
    ▼
Generate User Preference Embedding
    │
    ▼
Similarity Search
    │
    ▼
Relevant Movies Retrieved
    │
    ▼
Gemini Recommendation Generation
```

### Why this approach?

A traditional recommendation engine generally relies on exact genre or language matching. Such systems are limited because they cannot understand semantic similarity between movies.

Using embeddings enables the system to identify movies that are conceptually similar, allowing recommendations based on themes, storytelling style, or overall context rather than exact metadata.

---

# Project Structure

The project is organized using feature-based layering.

```text
config
constant
controller
dto
entity
event
exception
listener
mapper
repository
scheduler
security
service
strategy
util
validation
```

Each package has a well-defined responsibility, making the project easier to maintain and extend.

---

# Design Principles

The implementation emphasizes the following engineering principles:

* Single Responsibility Principle
* Separation of Concerns
* Dependency Injection
* Open/Closed Principle
* Composition over Inheritance
* Domain-Driven Package Organization
* Event-Driven Communication
* Interface-Based Design

The overall goal was to build a backend that is modular, testable, and easy to evolve without introducing unnecessary complexity.

---

# Scalability Considerations

Although the assignment explicitly excludes distributed systems, the design intentionally avoids decisions that would make future scaling difficult.

Examples include:

* UUID-based identifiers for all entities.
* Optimistic locking for concurrent updates.
* Asynchronous processing for notifications.
* Strategy-based business rules.
* Event-driven side effects.
* Decoupled AI integration.
* Repository abstraction for persistence.
* Service abstraction for business logic.

These choices allow the application to evolve toward a microservice or distributed architecture with minimal redesign while remaining simple enough for the scope of this assignment.
