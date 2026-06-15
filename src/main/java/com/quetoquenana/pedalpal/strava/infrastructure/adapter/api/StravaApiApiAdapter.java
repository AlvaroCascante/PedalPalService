package com.quetoquenana.pedalpal.strava.infrastructure.adapter.api;

import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.strava.config.StravaProperties;
import com.quetoquenana.pedalpal.strava.domain.model.*;
import com.quetoquenana.pedalpal.strava.domain.port.StravaApiClient;
import com.quetoquenana.pedalpal.strava.infrastructure.adapter.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Strava API adapter for OAuth and activity calls.
 */
@Slf4j
@Component
public class StravaApiApiAdapter implements StravaApiClient {

    private final RestClient restClient;
    private final StravaProperties properties;

    public StravaApiApiAdapter(RestClient stravaRestClient, StravaProperties properties) {
        this.restClient = stravaRestClient;
        this.properties = properties;
    }

    /**
     * Exchanges an authorization code for Strava tokens.
     */
    @Override
    public StravaToken exchangeAuthorizationCode(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", properties.getClientId());
        body.add("client_secret", properties.getClientSecret());
        body.add("code", code);
        body.add("grant_type", "authorization_code");

        StravaTokenResponse response = restClient.post()
                .uri(properties.getOauthBaseUrl() + "/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(StravaTokenResponse.class);

        if (response == null || response.getAccessToken() == null) {
            throw new BusinessException("strava.oauth.exchange.failed");
        }

        return toToken(response);
    }

    /**
     * Refreshes Strava access token using a refresh token.
     */
    @Override
    public StravaToken refreshAccessToken(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", properties.getClientId());
        body.add("client_secret", properties.getClientSecret());
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");

        StravaTokenResponse response = restClient.post()
                .uri(properties.getOauthBaseUrl() + "/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(StravaTokenResponse.class);

        if (response == null || response.getAccessToken() == null) {
            throw new BusinessException("strava.oauth.refresh.failed");
        }

        return toToken(response);
    }

    /**
     * Fetches a Strava activity by id.
     */
    @Override
    public StravaActivity getActivityById(Long activityId, String accessToken) {
        if (activityId == null || accessToken == null) {
            return null;
        }
        StravaActivityResponse response = restClient.get()
                .uri("/activities/{id}", activityId)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(StravaActivityResponse.class);

        if (response == null) {
            log.warn("Strava activity {} returned null", activityId);
            return null;
        }

        return StravaActivity.builder()
                .id(response.getId())
                .gearId(response.getGearId())
                .sportType(StravaSportType.from(response.getSportType()))
                .distanceMeters(response.getDistance())
                .movingTimeSeconds(response.getMovingTime())
                .build();
    }

    @Override
    public List<StravaAthleteBike> getAthleteBikes(String accessToken) {
        log.debug("Getting athlete bikes for access token: {}", accessToken);
        StravaAthleteResponse response =  restClient.get()
                .uri("/athlete")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        log.debug("Strava athlete response: {}", response);

        assert response != null;
        List<StravaAthleteBikeResponse> bikeResponse = response.getBikes() == null ? Collections.emptyList() : response.getBikes();
        return bikeResponse.stream().map(
                r -> StravaAthleteBike.builder()
                        .id(r.id())
                        .name(r.name())
                        .nickname(r.nickname())
                        .distance(BigDecimal.valueOf(r.distance()))
                        .primary(r.primary())
                        .retired(r.retired())
                        .build()
        ).toList();
    }

    @Override
    public StravaAthleteBikeDetail getBikeDetail(String bikeId, String accessToken) {
        log.debug("Getting bike detail for bikeId: {}", bikeId);
        StravaAthleteBikeDetailResponse response =  restClient.get()
                .uri("/gears/{id}", bikeId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        log.debug("Strava bike detail response: {}", response);
        assert response != null;
        return StravaAthleteBikeDetail.builder()
                .id(response.id())
                .primary(response.primary())
                .name(response.name())
                .nickname(response.nickname())
                .resourceState(response.resourceState())
                .retired(response.retired())
                .distance(BigDecimal.valueOf(response.distance()))
                .convertedDistance(BigDecimal.valueOf(response.convertedDistance()))
                .brandName(response.brandName())
                .modelName(response.modelName())
                .frameType(response.frameType())
                .description(response.description())
                .weight(response.weight())
                .build();
    }

    private StravaToken toToken(StravaTokenResponse response) {
        Long athleteId = response.getAthlete() == null ? null : response.getAthlete().getId();
        Instant expiresAt = response.getExpiresAt() == null ? null : Instant.ofEpochSecond(response.getExpiresAt());
        return StravaToken.builder()
                .accessToken(response.getAccessToken())
                .refreshToken(response.getRefreshToken())
                .expiresAt(expiresAt)
                .athleteId(athleteId)
                .scope(response.getScope())
                .build();
    }
}
