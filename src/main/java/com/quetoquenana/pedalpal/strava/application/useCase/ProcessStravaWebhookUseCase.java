package com.quetoquenana.pedalpal.strava.application.useCase;

import com.quetoquenana.pedalpal.common.application.port.BackgroundJobDispatcher;
import com.quetoquenana.pedalpal.strava.application.command.ProcessStravaWebhookCommand;
import com.quetoquenana.pedalpal.strava.application.command.SyncStravaActivityCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Use case that dispatches Strava webhook events for background processing.
 */
@Slf4j
@RequiredArgsConstructor
public class ProcessStravaWebhookUseCase {

    private final BackgroundJobDispatcher jobDispatcher;
    private final SyncStravaActivityUseCase syncStravaActivityUseCase;

    /**
     * Dispatches Strava webhook events to background processing.
     */
    public void execute(ProcessStravaWebhookCommand command) {
        if (command == null || command.event() == null) {
            return;
        }
        jobDispatcher.dispatch(() -> syncStravaActivityUseCase.execute(new SyncStravaActivityCommand(command.event())));
        log.info("Dispatched Strava webhook event {} for processing", command.event().getObjectId());
    }
}
