package com.quetoquenana.pedalpal.strava.application.useCase;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.strava.application.result.StravaConnectUrlResult;
import com.quetoquenana.pedalpal.strava.config.StravaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

import static com.quetoquenana.pedalpal.common.util.Constants.Strava.*;

/**
 * Use case that generates a Strava OAuth authorization URL.
 */
@Slf4j
@RequiredArgsConstructor
public class GetStravaConnectUrlUseCase {

    private final AuthenticatedUserPort authenticatedUserPort;
    private final StravaProperties properties;

    /**
     * Builds the Strava OAuth authorization URL for the current user.
     */
    public StravaConnectUrlResult execute() {
        AuthenticatedUser user = authenticatedUserPort.getAuthenticatedUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        String scope = properties.getScopes() == null ? "" : properties.getScopes();
        String url = UriComponentsBuilder.fromUriString(properties.getOauthBaseUrl())
                .path(AUTH_URL_PATH)
                .queryParam(AUTH_PARAM_CLIENT_ID, properties.getClientId())
                .queryParam(AUTH_PARAM_REDIRECT_URI, properties.getRedirectUri())
                .queryParam(AUTH_PARAM_RESPONSE_TYPE, AUTH_PARAM_RESPONSE_TYPE_CODE)
                .queryParam(AUTH_PARAM_APPROVAL_PROMPT, AUTH_PARAM_APPROVAL_PROMPT_AUTO)
                .queryParam(AUTH_PARAM_SCOPE, scope)
                .queryParam(AUTH_PARAM_STATE, user.userId())
                .build()
                .toUriString();

        log.info("Generated Strava connect URL for user {}", user.userId());
        // TODO: replace state with signed nonce persisted server-side for CSRF protection.
        return new StravaConnectUrlResult(url, user.userId().toString());
    }
}
