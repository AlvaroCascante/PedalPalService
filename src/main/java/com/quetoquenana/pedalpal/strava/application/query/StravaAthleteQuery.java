package com.quetoquenana.pedalpal.strava.application.query;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.strava.application.mapper.StravaMapper;
import com.quetoquenana.pedalpal.strava.application.result.StravaAthleteBikeResult;
import com.quetoquenana.pedalpal.strava.domain.model.StravaConnection;
import com.quetoquenana.pedalpal.strava.domain.model.StravaAthleteBike;
import com.quetoquenana.pedalpal.strava.domain.repository.StravaConnectionRepository;
import com.quetoquenana.pedalpal.strava.infrastructure.adapter.api.StravaApiApiAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Query for generating the Strava authorization URL.
 */
@RequiredArgsConstructor
@Slf4j
public class StravaAthleteQuery {

    private final AuthenticatedUserPort authenticatedUserPort;
    private final StravaConnectionRepository connectionRepository;
    private final StravaApiApiAdapter stravaApiAdapter;
    private final StravaMapper mapper;

    public List<StravaAthleteBikeResult> getAthleteBikes() {
        AuthenticatedUser user = authenticatedUserPort.getAuthenticatedUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));
        log.debug("Authenticated user: {}", user);

        StravaConnection connection = connectionRepository.findByUserId(user.userId())
                .orElseThrow(() -> new ForbiddenAccessException("authentication.strava.required"));
        log.debug("Strava connection: {}", connection);

        List<StravaAthleteBike> models = stravaApiAdapter.getAthleteBikes(connection.getAccessToken());
        return models.stream()
                .map(mapper::toResult)
                .toList();
    }
}