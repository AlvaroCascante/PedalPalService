package com.quetoquenana.pedalpal.strava.domain.port;

import com.quetoquenana.pedalpal.strava.domain.model.StravaActivity;
import com.quetoquenana.pedalpal.strava.domain.model.StravaAthleteBike;
import com.quetoquenana.pedalpal.strava.domain.model.StravaToken;

import java.util.List;

/**
 * Client port for calling Strava APIs.
 */
public interface StravaApiClient {

    /**
     * Exchanges an OAuth authorization code for access and refresh tokens.
     */
    StravaToken exchangeAuthorizationCode(String code);

    /**
     * Refreshes an access token using the provided refresh token.
     */
    StravaToken refreshAccessToken(String refreshToken);

    /**
     * Fetches a Strava activity by ID using the supplied access token.
     */
    StravaActivity getActivityById(Long activityId, String accessToken);

    List<StravaAthleteBike> getAthleteBikes(String accessToken);
}
