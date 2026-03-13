package com.quetoquenana.pedalpal.strava.presentation.controller;

import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
import com.quetoquenana.pedalpal.strava.application.useCase.ProcessStravaWebhookUseCase;
import com.quetoquenana.pedalpal.strava.config.StravaProperties;
import com.quetoquenana.pedalpal.strava.presentation.dto.request.StravaWebhookEventRequest;
import com.quetoquenana.pedalpal.strava.presentation.dto.response.StravaWebhookChallengeResponse;
import com.quetoquenana.pedalpal.strava.presentation.mapper.StravaApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Strava webhook verification and events.
 */
@RestController
@RequestMapping("/v1/api/strava/webhook")
@RequiredArgsConstructor
@Slf4j
public class StravaWebhookController {

    private final StravaProperties properties;
    private final ProcessStravaWebhookUseCase processStravaWebhookUseCase;
    private final StravaApiMapper apiMapper;

    /**
     * Handles Strava webhook verification challenge.
     */
    @GetMapping
    public ResponseEntity<StravaWebhookChallengeResponse> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String verifyToken,
            @RequestParam("hub.challenge") String challenge
    ) {
        if (!properties.getWebhookVerifyToken().equals(verifyToken)) {
            log.warn("Strava webhook verification failed");
            return ResponseEntity.status(403).build();
        }
        if (!"subscribe".equalsIgnoreCase(mode)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new StravaWebhookChallengeResponse(challenge));
    }

    /**
     * Receives Strava webhook events and dispatches background processing.
     */
    @PostMapping
    public ResponseEntity<ApiResponse> receiveEvent(@RequestBody StravaWebhookEventRequest request) {
        processStravaWebhookUseCase.execute(apiMapper.toCommand(request));
        return ResponseEntity.ok(new ApiResponse("Webhook received", 0));
    }
}
