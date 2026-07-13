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


# REST API Design

The project follows resource-oriented REST principles.

The API is grouped according to business domains.

Examples include:

```text
/api/v1/auth
/api/v1/users
/api/v1/cities
/api/v1/theaters
/api/v1/screens
/api/v1/seats
/api/v1/movies
/api/v1/shows
/api/v1/seat-holds
/api/v1/bookings
/api/v1/payments
/api/v1/discount-codes
/api/v1/refund-policies
/api/v1/notifications