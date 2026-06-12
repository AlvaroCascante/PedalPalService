package com.quetoquenana.pedalpal.strava.presentation.controller;

import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
import com.quetoquenana.pedalpal.strava.application.command.HandleStravaOAuthCallbackCommand;
import com.quetoquenana.pedalpal.strava.application.result.StravaConnectUrlResult;
import com.quetoquenana.pedalpal.strava.application.result.StravaConnectionStatusResult;
import com.quetoquenana.pedalpal.strava.application.useCase.GetStravaConnectUrlUseCase;
import com.quetoquenana.pedalpal.strava.config.StravaProperties;
import com.quetoquenana.pedalpal.strava.application.query.StravaConnectionStatusQuery;
import com.quetoquenana.pedalpal.strava.application.useCase.HandleStravaOAuthCallbackUseCase;
import com.quetoquenana.pedalpal.strava.presentation.dto.response.StravaConnectUrlResponse;
import com.quetoquenana.pedalpal.strava.presentation.dto.response.StravaConnectionStatusResponse;
import com.quetoquenana.pedalpal.strava.presentation.mapper.StravaApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * REST controller for Strava OAuth flows.
 */
@RestController
@RequestMapping("/v1/api/strava")
@RequiredArgsConstructor
@Slf4j
public class StravaAuthController {

    private static final String OAUTH_CALLBACK_TEMPLATE_PATH = "templates/strava/strava-oauth-callback.html";
    private static final String PLACEHOLDER_APP_LINK_URL = "__APP_LINK_URL__";
    private static final String PLACEHOLDER_DEEP_LINK_URL = "__DEEP_LINK_URL__";
    private static final String PLACEHOLDER_COMPLETION_MESSAGE = "__COMPLETION_MESSAGE__";
    private static final String PLACEHOLDER_DEEP_LINK_DELAY_MS = "__DEEP_LINK_DELAY_MS__";
    private static final String PLACEHOLDER_MESSAGE_DELAY_MS = "__MESSAGE_DELAY_MS__";

    private final GetStravaConnectUrlUseCase getStravaConnectUrlUseCase;
    private final StravaConnectionStatusQuery stravaConnectionStatusQuery;
    private final HandleStravaOAuthCallbackUseCase handleStravaOAuthCallbackUseCase;
    private final StravaApiMapper apiMapper;
    private final StravaProperties stravaProperties;

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
    // INFO -- This is the call back when the user auth strava, Token Exchange
    // https://developers.strava.com/docs/authentication/
    @GetMapping(value = "/oauth/callback", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> handleCallback(
            @RequestParam("code") String code,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "state", required = false) String state
    ) {
        log.info("Strava OAuth callback received for code exchange");
        log.debug("scope: {}, state: {}", scope, state);
        HandleStravaOAuthCallbackCommand command = new HandleStravaOAuthCallbackCommand(code, scope, state);

        try {
            StravaConnectionStatusResult result = handleStravaOAuthCallbackUseCase.execute(command);
            log.info("Strava OAuth callback completed for athleteId {}", result.athleteId());
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(buildOAuthCompletionHtml(state, "success", null, null));
        } catch (BusinessException ex) {
            String errorCode = ex.getMessageKey();
            log.error("Business error completing Strava OAuth callback for state {} with code {}", state, errorCode, ex);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(buildOAuthCompletionHtml(state, "error", errorCode, "oauth_exchange_failed"));
        } catch (RuntimeException ex) {
            log.error("Failed to complete Strava OAuth callback for state {}", state, ex);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(buildOAuthCompletionHtml(state, "error", "unexpected.error", "unexpected_callback_error"));
        }
    }

    private String buildOAuthCompletionHtml(String state, String status, String errorCode, String reason) {
        String appLinkUrl = buildCallbackUrl(stravaProperties.getMobileCallbackAppLinkUrl(), state, status, errorCode, reason);
        String deepLinkUrl = buildCallbackUrl(stravaProperties.getMobileCallbackDeepLinkUrl(), state, status, errorCode, reason);
        String completionMessage = Objects.equals(status, "success")
                ? "Authorization completed. Return to the app."
                : "Authorization failed. Return to the app and try again.";

        String response = readOauthCallbackTemplate()
                .replace(PLACEHOLDER_APP_LINK_URL, escapeForJsSingleQuotedString(appLinkUrl))
                .replace(PLACEHOLDER_DEEP_LINK_URL, escapeForJsSingleQuotedString(deepLinkUrl))
                .replace(PLACEHOLDER_COMPLETION_MESSAGE, escapeForJsSingleQuotedString(completionMessage))
                .replace(PLACEHOLDER_DEEP_LINK_DELAY_MS, Long.toString(stravaProperties.getMobileCallbackDeepLinkDelayMs()))
                .replace(PLACEHOLDER_MESSAGE_DELAY_MS, Long.toString(stravaProperties.getMobileCallbackMessageDelayMs()));
        log.debug("buildOAuthCompletionHtml response: {}", response);
        return response;
    }

    private String readOauthCallbackTemplate() {
        ClassPathResource resource = new ClassPathResource(OAUTH_CALLBACK_TEMPLATE_PATH);
        try {
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read OAuth callback template", ex);
        }
    }

    private String buildCallbackUrl(String baseUrl, String state, String status, String errorCode, String reason) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("status", status);

        if (state != null && !state.isBlank()) {
            builder.queryParam("state", state);
        }
        if (errorCode != null && !errorCode.isBlank()) {
            builder.queryParam("errorCode", errorCode);
        }
        if (reason != null && !reason.isBlank()) {
            builder.queryParam("reason", reason);
        }

        return builder.build().toUriString();
    }

    private String escapeForJsSingleQuotedString(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("'", "\\'");
    }
}
