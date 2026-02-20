package com.quetoquenana.pedalpal.job;

import com.quetoquenana.pedalpal.config.MaintenanceSuggestionJobProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MaintenanceSuggestionJob {

    private final MaintenanceSuggestionJobProperties properties;

    // Scheduled to run according to configured cron. Method checks enabled flag before running.
    @Scheduled(cron = "${jobs.maintenance-suggestions.cron}")
    public void scheduledRun() {
        if (!properties.isEnabled()) {
            log.info("MaintenanceSuggestionJob scheduled run skipped because job is disabled");
            return;
        }
        log.info("MaintenanceSuggestionJob scheduled run starting (cron={})", properties.getCron());
        try {
            //maintenanceSuggestionService.generateBikesSuggestions();
            log.info("MaintenanceSuggestionJob scheduled run completed");
        } catch (Exception ex) {
            log.error("MaintenanceSuggestionJob scheduled run failed: {}", ex.getMessage(), ex);
        }
    }
}
