package com.kodekittu.movieticketbooking.scheduler;

import com.kodekittu.movieticketbooking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRetryScheduler {

    private static final int BATCH_SIZE = 50;

    private final NotificationService notificationService;

    @Scheduled(fixedDelayString = "PT5M")
    public void retryFailedNotifications() {
        int retried = notificationService.retryFailed(BATCH_SIZE);
        if (retried > 0) {
            log.info("Retried {} failed notifications", retried);
        }
    }
}
