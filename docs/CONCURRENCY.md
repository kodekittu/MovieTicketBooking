# Concurrency Decisions

The system prevents double booking through optimistic locking on `ShowSeat`.

- `ShowSeat` has a `@Version` column and is the only mutable per-show seat inventory row.
- Seat hold creation loads selected rows with an optimistic lock and changes `AVAILABLE -> HELD`.
- Payment confirmation loads held rows with `OPTIMISTIC_FORCE_INCREMENT` and changes `HELD -> BOOKED`.
- Cancellation releases booked rows back to `AVAILABLE` while preserving historical `BookingSeat` rows.
- Booking-critical operations run inside `TransactionTemplate` and are wrapped by `RetryExecutor`.
- Retry is limited to three attempts and only retries optimistic lock failures.
- Business conflicts such as held, booked, expired, or invalid seats return `409 Conflict` or `422 Unprocessable Entity`; they are not retried.

This is intentionally optimistic-lock based because the assignment explicitly requires optimistic locking. In a very high-contention production system, PostgreSQL row-level pessimistic locks for seat acquisition would also be reasonable, but this implementation keeps the concurrency mechanism aligned with the stated requirement.
