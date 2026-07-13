# PROJECT_DECISIONS.md

# Engineering Decisions

This document explains some of the key technical decisions made while implementing the Movie Ticket Booking System and the reasoning behind those choices.

---

# Why PostgreSQL?

The system models a highly relational domain involving users, theatres, screens, seats, shows, bookings, payments, pricing rules, and refund policies. A relational database is a natural fit for this type of data model because it provides strong consistency, referential integrity, and transactional guarantees.

PostgreSQL was selected because it offers excellent support for ACID transactions, row-level locking, indexing, and complex relationships, all of which are important for a booking system where concurrent updates must be handled correctly.

From a development perspective, PostgreSQL was also a practical choice because I was already familiar with it and had a local environment configured, allowing me to focus more on the application design rather than database setup.

---

# Why Optimistic Locking?

Multiple users may attempt to reserve or book the same seat simultaneously.

For this project, optimistic locking was chosen because booking conflicts are expected to be relatively infrequent compared to the number of successful booking operations.

Using JPA's `@Version` field allows conflicting updates to be detected without keeping database rows locked for long periods. This improves scalability while still preventing inconsistent updates such as double-booking.

For a production system with extremely high contention, additional mechanisms such as distributed locking or database `SKIP LOCKED` strategies could be introduced, but optimistic locking provides a clean and efficient solution within the scope of this assignment.

---

# Why Spring Application Events?

The booking workflow contains several secondary operations that should not increase the response time experienced by the customer.

Examples include:

* Notification generation
* Future analytics
* Audit logging
* AI workflow triggers

Spring Application Events allow these responsibilities to be separated from the primary booking transaction.

This results in:

* Better separation of concerns
* Loosely coupled components
* Easier feature expansion
* Cleaner service implementations

Using events also keeps the booking service focused solely on booking-related business logic.

---

# Why Google Gemini?

The recommendation engine required a Large Language Model capable of generating semantic embeddings and producing natural language recommendations.

Google Gemini provides both capabilities through a straightforward API, making it well suited for integrating AI features into a Spring Boot application.

Another practical consideration was the availability of a generous free usage tier through Google AI Studio, which made it possible to experiment, iterate, and validate the recommendation workflow without introducing additional infrastructure costs.

The application uses Gemini for:

* Movie embedding generation
* Recommendation generation
* Prompt-based contextual responses

The core booking logic remains completely independent of the AI layer.

---

# Why Pinecone?

Semantic search requires efficient storage and retrieval of high-dimensional vector embeddings.

Pinecone was selected because it is a managed vector database that provides simple APIs for indexing and similarity search without requiring additional infrastructure management.

I had previous experience working with Pinecone, which allowed the AI integration to be completed quickly while maintaining a clean architecture.

Another advantage of this design is that embeddings are generated once and stored inside Pinecone. Recommendation requests reuse the stored vectors instead of repeatedly generating embeddings, reducing latency and API usage.

---

# Why Retrieval-Augmented Generation (RAG)?

Instead of sending the entire movie catalogue to the language model for every recommendation, the system first performs semantic retrieval using Pinecone.

The workflow is:

Movie Metadata

↓

Gemini Embeddings

↓

Pinecone Similarity Search

↓

Relevant Movies

↓

Gemini Recommendation

This approach has several advantages:

* Better recommendation quality
* Lower token usage
* Reduced response time
* Improved scalability as the catalogue grows

Separating retrieval from generation also keeps the recommendation engine modular and easier to evolve.

---

# Why a Layered Architecture?

The project follows a layered architecture consisting of Controllers, Services, Repositories, and Infrastructure components.

Each layer has a clearly defined responsibility.

This separation improves:

* Readability
* Testability
* Maintainability
* Future extensibility

It also allows business logic to remain independent of HTTP APIs and persistence implementations.

---

# Why Strategy Pattern?

Pricing rules, discounts, refunds, and notification mechanisms are business rules that may change over time.

Instead of embedding conditional logic inside service classes, these responsibilities are modeled using the Strategy Pattern.

This makes it straightforward to introduce new pricing models, promotional campaigns, or refund policies without modifying existing business logic.

---

# Design Philosophy

While implementing this project, the primary objective was not simply to build a working CRUD application, but to demonstrate how a production-oriented backend can be designed with clean architecture, transactional consistency, extensibility, and modern AI capabilities.

Where practical, implementation choices favored maintainability and clear separation of responsibilities over unnecessary complexity. The resulting design keeps the booking workflow reliable while allowing features such as notifications, pricing rules, and AI recommendations to evolve independently.
