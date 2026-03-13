package com.quetoquenana.pedalpal.strava.presentation.controller;

import com.quetoquenana.pedalpal.common.presentation.dto.response.ApiResponse;
import com.quetoquenana.pedalpal.strava.application.query.StravaAthleteQuery;
import com.quetoquenana.pedalpal.strava.application.result.StravaAthleteBikeResult;
import com.quetoquenana.pedalpal.strava.presentation.dto.response.StravaAthleteBikeResponse;
import com.quetoquenana.pedalpal.strava.presentation.mapper.StravaApiMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for Strava webhook verification and events.
 */
@RestController
@RequestMapping("/v1/api/strava")
@RequiredArgsConstructor
@Slf4j
public class StravaController {

    private final StravaAthleteQuery stravaAthleteQuery;
    private final StravaApiMapper apiMapper;

    @GetMapping("/bikes")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> getAthleteBikes() {
        log.info("GET /v1/api/strava/bikes Received request to find Athlete Bikes in strava");
        List<StravaAthleteBikeResult> result = stravaAthleteQuery.getAthleteBikes();
        List<StravaAthleteBikeResponse> response = result.stream().map(apiMapper::toResponsea).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }
}
