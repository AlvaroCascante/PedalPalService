package com.quetoquenana.pedalpal.strava.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.strava.domain.model.StravaActivitySync;
import com.quetoquenana.pedalpal.strava.domain.model.StravaConnection;
import com.quetoquenana.pedalpal.strava.infrastructure.persistence.entity.StravaActivitySyncEntity;
import com.quetoquenana.pedalpal.strava.infrastructure.persistence.entity.StravaConnectionEntity;

/**
 * Maps Strava persistence entities to domain models and back.
 */
public class StravaEntityMapper {

    private StravaEntityMapper() {}

    public static StravaConnection toModel(StravaConnectionEntity entity) {
        if (entity == null) {
            return null;
        }
        return StravaConnection.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .stravaAthleteId(entity.getStravaAthleteId())
                .accessToken(entity.getAccessToken())
                .refreshToken(entity.getRefreshToken())
                .tokenExpiresAt(entity.getTokenExpiresAt())
                .scope(entity.getScope())
                .status(entity.getStatus())
                .build();
    }

    public static StravaConnectionEntity toEntity(StravaConnection model) {
        if (model == null) {
            return null;
        }
        StravaConnectionEntity entity = StravaConnectionEntity.builder()
                .id(model.getId())
                .userId(model.getUserId())
                .stravaAthleteId(model.getStravaAthleteId())
                .accessToken(model.getAccessToken())
                .refreshToken(model.getRefreshToken())
                .tokenExpiresAt(model.getTokenExpiresAt())
                .scope(model.getScope())
                .status(model.getStatus())
                .build();
        entity.setCreatedBy(model.getUserId());
        entity.setUpdatedBy(model.getUserId());
        return entity;
    }

    public static StravaActivitySync toModel(StravaActivitySyncEntity entity) {
        if (entity == null) {
            return null;
        }
        return StravaActivitySync.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .bikeId(entity.getBikeId())
                .stravaActivityId(entity.getStravaActivityId())
                .stravaAthleteId(entity.getStravaAthleteId())
                .distanceKmDelta(entity.getDistanceKmDelta())
                .movingTimeMinutesDelta(entity.getMovingTimeMinutesDelta())
                .originalDistanceMeters(entity.getOriginalDistanceMeters())
                .originalMovingTimeSeconds(entity.getOriginalMovingTimeSeconds())
                .sportType(entity.getSportType())
                .status(entity.getStatus())
                .syncedAt(entity.getSyncedAt())
                .build();
    }

    public static StravaActivitySyncEntity toEntity(StravaActivitySync model) {
        if (model == null) {
            return null;
        }
        StravaActivitySyncEntity entity = StravaActivitySyncEntity.builder()
                .id(model.getId())
                .userId(model.getUserId())
                .bikeId(model.getBikeId())
                .stravaActivityId(model.getStravaActivityId())
                .stravaAthleteId(model.getStravaAthleteId())
                .distanceKmDelta(model.getDistanceKmDelta())
                .movingTimeMinutesDelta(model.getMovingTimeMinutesDelta())
                .originalDistanceMeters(model.getOriginalDistanceMeters())
                .originalMovingTimeSeconds(model.getOriginalMovingTimeSeconds())
                .sportType(model.getSportType())
                .status(model.getStatus())
                .syncedAt(model.getSyncedAt())
                .build();
        entity.setCreatedBy(model.getUserId());
        entity.setUpdatedBy(model.getUserId());
        return entity;
    }
}
