package com.kodekittu.movieticketbooking.util;

import jakarta.persistence.OptimisticLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class RetryExecutor {

    private static final int MAX_ATTEMPTS = 3;

    public <T> T executeOnOptimisticLock(Supplier<T> operation) {
        RuntimeException lastFailure = null;
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                return operation.get();
            } catch (OptimisticLockingFailureException | OptimisticLockException exception) {
                lastFailure = exception;
                backoff(attempt);
            }
        }
        throw lastFailure;
    }

    private void backoff(int attempt) {
        try {
            Thread.sleep(100L * attempt);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while retrying optimistic lock operation", exception);
        }
    }
}
