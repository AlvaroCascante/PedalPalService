package com.quetoquenana.pedalpal.strava.application.query;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.strava.application.mapper.StravaMapper;
import com.quetoquenana.pedalpal.strava.application.result.StravaConnectionStatusResult;
import com.quetoquenana.pedalpal.strava.domain.model.StravaConnection;
import com.quetoquenana.pedalpal.strava.domain.repository.StravaConnectionRepository;
import lombok.RequiredArgsConstructor;

/**
 * Use case that reports the Strava connection status for the current user.
 */

@RequiredArgsConstructor
public class StravaConnectionStatusQuery {

    private final AuthenticatedUserPort authenticatedUserPort;
    private final StravaConnectionRepository connectionRepository;
    private final StravaMapper mapper;

    /**
     * Returns the connection status for the authenticated user.
     */
    public StravaConnectionStatusResult getConnectionStatus() {
        AuthenticatedUser user = authenticatedUserPort.getAuthenticatedUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        StravaConnection connection = connectionRepository.findByUserId(user.userId()).orElse(null);
        return mapper.toResult(connection);
    }
}
