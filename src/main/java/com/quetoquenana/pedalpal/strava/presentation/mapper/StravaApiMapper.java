package com.quetoquenana.pedalpal.strava.presentation.mapper;

import com.quetoquenana.pedalpal.strava.application.command.HandleStravaOAuthCallbackCommand;
import com.quetoquenana.pedalpal.strava.application.command.ProcessStravaWebhookCommand;
import com.quetoquenana.pedalpal.strava.application.result.StravaConnectUrlResult;
import com.quetoquenana.pedalpal.strava.application.result.StravaConnectionStatusResult;
import com.quetoquenana.pedalpal.strava.application.result.StravaAthleteBikeResult;
import com.quetoquenana.pedalpal.strava.domain.model.StravaWebhookEvent;
import com.quetoquenana.pedalpal.strava.presentation.dto.request.StravaWebhookEventRequest;
import com.quetoquenana.pedalpal.strava.presentation.dto.response.StravaConnectUrlResponse;
import com.quetoquenana.pedalpal.strava.presentation.dto.response.StravaConnectionStatusResponse;
import com.quetoquenana.pedalpal.strava.presentation.dto.response.StravaAthleteBikeResponse;

import java.time.Instant;

/**
 * Presentation mapper for Strava endpoints.
 */
public class StravaApiMapper {

    /**
     * Maps OAuth callback parameters into a command.
     */
    public HandleStravaOAuthCallbackCommand toCommand(String code, String scope, String state) {
        return new HandleStravaOAuthCallbackCommand(code, scope, state);
    }

    /**
     * Maps webhook request into a command.
     */
    public ProcessStravaWebhookCommand toCommand(StravaWebhookEventRequest request) {
        return new ProcessStravaWebhookCommand(toWebhookEvent(request));
    }

    /**
     * Maps Strava connect result to response.
     */
    public StravaConnectUrlResponse toResponse(StravaConnectUrlResult result) {
        return new StravaConnectUrlResponse(result.url(), result.state());
    }

    /**
     * Maps Strava status result to response.
     */
    public StravaConnectionStatusResponse toResponse(StravaConnectionStatusResult result) {
        return new StravaConnectionStatusResponse(result.connected(), result.status(), result.athleteId(), result.scope());
    }

    private StravaWebhookEvent toWebhookEvent(StravaWebhookEventRequest request) {
        if (request == null) {
            return null;
        }
        Instant eventTime = request.getEventTime() == null ? null : Instant.ofEpochSecond(request.getEventTime());
        return StravaWebhookEvent.builder()
                .objectType(request.getObjectType())
                .aspectType(request.getAspectType())
                .objectId(request.getObjectId())
                .ownerId(request.getOwnerId())
                .updates(request.getUpdates())
                .eventTime(eventTime)
                .build();
    }

    public StravaAthleteBikeResponse toResponsea(StravaAthleteBikeResult result) {
        return new StravaAthleteBikeResponse(
                result.id(),
                result.name(),
                result.nickname(),
                result.primary(),
                result.retired(),
                result.distance()
        );
    }
}
