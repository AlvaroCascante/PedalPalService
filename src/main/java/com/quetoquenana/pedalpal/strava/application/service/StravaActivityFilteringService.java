package com.quetoquenana.pedalpal.strava.application.service;

import com.quetoquenana.pedalpal.strava.domain.model.StravaSportType;

/**
 * Application service for deciding which Strava activities should sync.
 */
public class StravaActivityFilteringService {

    /**
     * @return true when the activity is cycling-related.
     */
    public boolean isCyclingActivity(StravaSportType sportType) {
        return sportType == StravaSportType.RIDE
                || sportType == StravaSportType.VIRTUAL_RIDE
                || sportType == StravaSportType.E_BIKE_RIDE;
    }
}
