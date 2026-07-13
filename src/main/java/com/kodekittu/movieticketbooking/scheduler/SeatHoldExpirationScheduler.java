package com.kodekittu.movieticketbooking.scheduler;

import com.kodekittu.movieticketbooking.service.SeatHoldService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatHoldExpirationScheduler {

    private static final int BATCH_SIZE = 100;

    private final SeatHoldService seatHoldService;

    @Scheduled(cron = "${app.seat-hold.expiration-scheduler-cron}")
    public void releaseExpiredSeatHolds() {
        int expired = seatHoldService.expireDueHolds(BATCH_SIZE);
        if (expired > 0) {
            log.info("Released {} expired seat holds", expired);
        }
    }
}
