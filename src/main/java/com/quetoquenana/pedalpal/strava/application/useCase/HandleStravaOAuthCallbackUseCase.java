package com.quetoquenana.pedalpal.strava.application.useCase;

import com.quetoquenana.pedalpal.strava.application.command.HandleStravaOAuthCallbackCommand;
import com.quetoquenana.pedalpal.strava.application.result.StravaConnectionStatusResult;
import com.quetoquenana.pedalpal.strava.domain.model.StravaConnection;
import com.quetoquenana.pedalpal.strava.domain.model.StravaConnectionStatus;
import com.quetoquenana.pedalpal.strava.domain.model.StravaToken;
import com.quetoquenana.pedalpal.strava.domain.port.StravaApiClient;
import com.quetoquenana.pedalpal.strava.domain.repository.StravaConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Use case for handling Strava OAuth callback codes and storing tokens.
 */
@Slf4j
@Transactional
@RequiredArgsConstructor
public class HandleStravaOAuthCallbackUseCase {

    private final StravaApiClient stravaApiClient;
    private final StravaConnectionRepository connectionRepository;

    /**
     * Exchanges the authorization code for tokens and persists the connection.
     */
    public StravaConnectionStatusResult execute(HandleStravaOAuthCallbackCommand command) {
        UUID userId = UUID.fromString(command.state());

        StravaToken token = stravaApiClient.exchangeAuthorizationCode(command.code());
        Optional<StravaConnection> existing = connectionRepository.findByUserId(userId);

        StravaConnection connection = existing.orElseGet(StravaConnection::new);
        connection.setUserId(userId);
        connection.setStravaAthleteId(token.getAthleteId());
        connection.setAccessToken(token.getAccessToken());
        connection.setRefreshToken(token.getRefreshToken());
        connection.setTokenExpiresAt(token.getExpiresAt());
        connection.setScope(token.getScope());
        connection.setStatus(StravaConnectionStatus.CONNECTED);

        connectionRepository.save(connection);
        log.info("Stored Strava connection for user {}", userId);

        // TODO: validate OAuth state param against stored nonce for CSRF protection.
        return new StravaConnectionStatusResult(true, connection.getStatus(), connection.getStravaAthleteId(), connection.getScope());
    }
}
