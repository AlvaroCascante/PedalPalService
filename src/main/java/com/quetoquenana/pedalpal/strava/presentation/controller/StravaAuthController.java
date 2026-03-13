package com.quetoquenana.pedalpal.strava.presentation.controller;

import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
import com.quetoquenana.pedalpal.strava.application.command.HandleStravaOAuthCallbackCommand;
import com.quetoquenana.pedalpal.strava.application.result.StravaConnectUrlResult;
import com.quetoquenana.pedalpal.strava.application.result.StravaConnectionStatusResult;
import com.quetoquenana.pedalpal.strava.application.useCase.GetStravaConnectUrlUseCase;
import com.quetoquenana.pedalpal.strava.application.query.StravaConnectionStatusQuery;
import com.quetoquenana.pedalpal.strava.application.useCase.HandleStravaOAuthCallbackUseCase;
import com.quetoquenana.pedalpal.strava.presentation.dto.response.StravaConnectUrlResponse;
import com.quetoquenana.pedalpal.strava.presentation.dto.response.StravaConnectionStatusResponse;
import com.quetoquenana.pedalpal.strava.presentation.mapper.StravaApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Strava OAuth flows.
 */
@RestController
@RequestMapping("/v1/api/strava")
@RequiredArgsConstructor
@Slf4j
public class StravaAuthController {

    private final GetStravaConnectUrlUseCase getStravaConnectUrlUseCase;
    private final StravaConnectionStatusQuery stravaConnectionStatusQuery;
    private final HandleStravaOAuthCallbackUseCase handleStravaOAuthCallbackUseCase;
    private final StravaApiMapper apiMapper;

    /**
     * Returns the Strava authorization URL for the authenticated user.
     */
    @GetMapping("/connect-url")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> getConnectUrl() {
        StravaConnectUrlResult result = getStravaConnectUrlUseCase.execute();
        StravaConnectUrlResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    /**
     * Returns the Strava connection status for the authenticated user.
     */
    @GetMapping("/connection/status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> getConnectionStatus() {
        StravaConnectionStatusResult result = stravaConnectionStatusQuery.getConnectionStatus();
        StravaConnectionStatusResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    /**
     * Handles Strava OAuth callback code exchange.
     */
    @GetMapping("/oauth/callback")
    public ResponseEntity<ApiResponse> handleCallback(
            @RequestParam("code") String code,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "state", required = false) String state
    ) {
        log.info("Strava OAuth callback received for code exchange");
        HandleStravaOAuthCallbackCommand command = new HandleStravaOAuthCallbackCommand(code, scope, state);
        StravaConnectionStatusResult result = handleStravaOAuthCallbackUseCase.execute(command);
        StravaConnectionStatusResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }
}
