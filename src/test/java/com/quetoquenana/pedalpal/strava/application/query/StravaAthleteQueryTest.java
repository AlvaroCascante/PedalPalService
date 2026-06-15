package com.quetoquenana.pedalpal.strava.application.query;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.strava.application.mapper.StravaMapper;
import com.quetoquenana.pedalpal.strava.application.result.StravaAthleteBikeResult;
import com.quetoquenana.pedalpal.strava.domain.model.StravaAthleteBike;
import com.quetoquenana.pedalpal.strava.domain.model.StravaAthleteBikeDetail;
import com.quetoquenana.pedalpal.strava.domain.model.StravaConnection;
import com.quetoquenana.pedalpal.strava.domain.repository.StravaConnectionRepository;
import com.quetoquenana.pedalpal.strava.infrastructure.adapter.api.StravaApiApiAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StravaAthleteQueryTest {

    @Mock
    private AuthenticatedUserPort authenticatedUserPort;

    @Mock
    private StravaConnectionRepository connectionRepository;

    @Mock
    private StravaApiApiAdapter stravaApiAdapter;

    @Mock
    private StravaMapper mapper;

    private StravaAthleteQuery query;

    @BeforeEach
    void setUp() {
        query = new StravaAthleteQuery(authenticatedUserPort, connectionRepository, stravaApiAdapter, mapper);
    }

    @Test
    void shouldThrowWhenUserIsNotAuthenticated() {
        when(authenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.empty());

        assertThrows(ForbiddenAccessException.class, () -> query.getAthleteBikes());

        verifyNoInteractions(connectionRepository, stravaApiAdapter, mapper);
    }

    @Test
    void shouldThrowWhenAuthenticatedUserHasNoStravaConnection() {
        UUID userId = UUID.randomUUID();
        AuthenticatedUser user = new AuthenticatedUser(userId, "test-user", "Test User", UserType.CUSTOMER);
        when(authenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(connectionRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(ForbiddenAccessException.class, () -> query.getAthleteBikes());

        verify(connectionRepository).findByUserId(userId);
        verifyNoInteractions(stravaApiAdapter, mapper);
    }

    @Test
    void shouldCallBikeDetailApiForEachBikeAndMapResponse() {
        UUID userId = UUID.randomUUID();
        String accessToken = "token-123";
        AuthenticatedUser user = new AuthenticatedUser(userId, "test-user", "Test User", UserType.CUSTOMER);
        StravaConnection connection = StravaConnection.builder().userId(userId).accessToken(accessToken).build();

        StravaAthleteBike bikeOne = StravaAthleteBike.builder().id("bike-1").build();
        StravaAthleteBike bikeTwo = StravaAthleteBike.builder().id("bike-2").build();

        StravaAthleteBikeDetail bikeOneDetail = StravaAthleteBikeDetail.builder()
                .id("bike-1")
                .distance(BigDecimal.valueOf(12000))
                .build();
        StravaAthleteBikeDetail bikeTwoDetail = StravaAthleteBikeDetail.builder()
                .id("bike-2")
                .distance(BigDecimal.valueOf(45000))
                .build();

        StravaAthleteBikeResult bikeOneResult = new StravaAthleteBikeResult(
                "bike-1", "Road Bike", "RB", true, false,
                BigDecimal.valueOf(12000), BigDecimal.valueOf(12), "Brand", "Model", 1, "desc"
        );
        StravaAthleteBikeResult bikeTwoResult = new StravaAthleteBikeResult(
                "bike-2", "MTB", "MTB", false, false,
                BigDecimal.valueOf(45000), BigDecimal.valueOf(45), "Brand2", "Model2", 2, "desc2"
        );

        when(authenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(connectionRepository.findByUserId(userId)).thenReturn(Optional.of(connection));
        when(stravaApiAdapter.getAthleteBikes(accessToken)).thenReturn(List.of(bikeOne, bikeTwo));
        when(stravaApiAdapter.getBikeDetail("bike-1", accessToken)).thenReturn(bikeOneDetail);
        when(stravaApiAdapter.getBikeDetail("bike-2", accessToken)).thenReturn(bikeTwoDetail);
        when(mapper.toResult(bikeOneDetail)).thenReturn(bikeOneResult);
        when(mapper.toResult(bikeTwoDetail)).thenReturn(bikeTwoResult);

        List<StravaAthleteBikeResult> response = query.getAthleteBikes();

        assertEquals(List.of(bikeOneResult, bikeTwoResult), response);
        verify(stravaApiAdapter).getAthleteBikes(accessToken);
        verify(stravaApiAdapter).getBikeDetail("bike-1", accessToken);
        verify(stravaApiAdapter).getBikeDetail("bike-2", accessToken);
        verify(mapper).toResult(bikeOneDetail);
        verify(mapper).toResult(bikeTwoDetail);
    }

    @Test
    void shouldReturnEmptyListWhenAthleteHasNoBikes() {
        UUID userId = UUID.randomUUID();
        String accessToken = "token-123";
        AuthenticatedUser user = new AuthenticatedUser(userId, "test-user", "Test User", UserType.CUSTOMER);
        StravaConnection connection = StravaConnection.builder().userId(userId).accessToken(accessToken).build();

        when(authenticatedUserPort.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(connectionRepository.findByUserId(userId)).thenReturn(Optional.of(connection));
        when(stravaApiAdapter.getAthleteBikes(accessToken)).thenReturn(Collections.emptyList());

        List<StravaAthleteBikeResult> response = query.getAthleteBikes();

        assertEquals(Collections.emptyList(), response);
        verify(stravaApiAdapter).getAthleteBikes(accessToken);
        verify(stravaApiAdapter, never()).getBikeDetail(anyString(), anyString());
        verifyNoInteractions(mapper);
    }
}

