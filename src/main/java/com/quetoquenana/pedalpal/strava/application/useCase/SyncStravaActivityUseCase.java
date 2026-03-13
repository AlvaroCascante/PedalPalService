package com.quetoquenana.pedalpal.strava.application.useCase;

import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.strava.application.command.SyncStravaActivityCommand;
import com.quetoquenana.pedalpal.strava.application.result.StravaSyncResult;
import com.quetoquenana.pedalpal.strava.application.service.StravaActivityFilteringService;
import com.quetoquenana.pedalpal.strava.application.service.StravaBikeMatchingService;
import com.quetoquenana.pedalpal.strava.application.service.StravaTokenService;
import com.quetoquenana.pedalpal.strava.domain.model.StravaActivity;
import com.quetoquenana.pedalpal.strava.domain.model.StravaActivitySync;
import com.quetoquenana.pedalpal.strava.domain.model.StravaConnection;
import com.quetoquenana.pedalpal.strava.domain.model.StravaSportType;
import com.quetoquenana.pedalpal.strava.domain.model.StravaSyncStatus;
import com.quetoquenana.pedalpal.strava.domain.repository.StravaActivitySyncRepository;
import com.quetoquenana.pedalpal.strava.domain.port.StravaApiClient;
import com.quetoquenana.pedalpal.strava.domain.repository.StravaConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Optional;

/**
 * Use case that syncs a Strava activity into bike usage metrics.
 */
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SyncStravaActivityUseCase {

    private final StravaConnectionRepository connectionRepository;
    private final StravaActivitySyncRepository activitySyncRepository;
    private final StravaApiClient stravaApiClient;
    private final StravaTokenService tokenService;
    private final StravaBikeMatchingService bikeMatchingService;
    private final StravaActivityFilteringService filteringService;
    private final BikeRepository bikeRepository;

    /**
     * Syncs the activity referenced by the webhook event.
     */
    public StravaSyncResult execute(SyncStravaActivityCommand command) {
        if (command == null || command.event() == null) {
            return new StravaSyncResult(StravaSyncStatus.SKIPPED, "Missing webhook event", null);
        }

        if (!command.event().isActivityEvent()) {
            return new StravaSyncResult(StravaSyncStatus.SKIPPED, "Non-activity webhook event", command.event().getObjectId());
        }

        if ("delete".equalsIgnoreCase(command.event().getAspectType())) {
            // TODO: apply reverse delta when delete events are supported.
            return new StravaSyncResult(StravaSyncStatus.SKIPPED, "Delete events not supported yet", command.event().getObjectId());
        }

        StravaConnection connection = connectionRepository.findByStravaAthleteId(command.event().getOwnerId())
                .orElse(null);
        if (connection == null) {
            return new StravaSyncResult(StravaSyncStatus.SKIPPED, "No Strava connection found", command.event().getObjectId());
        }

        String accessToken = tokenService.getValidAccessToken(connection);
        StravaActivity activity = stravaApiClient.getActivityById(command.event().getObjectId(), accessToken);
        if (activity == null) {
            return new StravaSyncResult(StravaSyncStatus.FAILED, "Failed to load Strava activity", command.event().getObjectId());
        }

        StravaSportType sportType = activity.getSportType();
        if (!filteringService.isCyclingActivity(sportType)) {
            return new StravaSyncResult(StravaSyncStatus.SKIPPED, "Non-cycling activity", activity.getId());
        }

        Optional<Bike> bikeOptional = bikeMatchingService.findBike(connection.getUserId(), activity.getGearId());
        if (bikeOptional.isEmpty()) {
            return new StravaSyncResult(StravaSyncStatus.SKIPPED, "No bike match for Strava gear", activity.getId());
        }
        Bike bike = bikeOptional.get();
        if (!bike.isExternalSync()) {
            return new StravaSyncResult(StravaSyncStatus.SKIPPED, "Bike external sync disabled", activity.getId());
        }

        Optional<StravaActivitySync> existingOpt = activitySyncRepository.findByStravaActivityId(activity.getId());
        int distanceKm = toKilometers(activity.getDistanceMeters());
        int movingMinutes = toMinutes(activity.getMovingTimeSeconds());

        if (existingOpt.isPresent()) {
            StravaActivitySync existing = existingOpt.get();
            if (sameOriginalValues(existing, activity)) {
                return new StravaSyncResult(StravaSyncStatus.SKIPPED, "Activity already synced", activity.getId());
            }
            int distanceDelta = distanceKm - safeInt(existing.getDistanceKmDelta());
            int timeDelta = movingMinutes - safeInt(existing.getMovingTimeMinutesDelta());

            applyBikeUsageDelta(bike, distanceDelta, timeDelta);
            updateSyncRecord(existing, activity, distanceKm, movingMinutes);
            bikeRepository.save(bike);
            activitySyncRepository.save(existing);

            return new StravaSyncResult(StravaSyncStatus.APPLIED, "Activity updated with delta", activity.getId());
        }

        applyBikeUsageDelta(bike, distanceKm, movingMinutes);
        StravaActivitySync sync = StravaActivitySync.builder()
                .userId(connection.getUserId())
                .bikeId(bike.getId())
                .stravaActivityId(activity.getId())
                .stravaAthleteId(connection.getStravaAthleteId())
                .distanceKmDelta(distanceKm)
                .movingTimeMinutesDelta(movingMinutes)
                .originalDistanceMeters(activity.getDistanceMeters())
                .originalMovingTimeSeconds(activity.getMovingTimeSeconds())
                .sportType(activity.getSportType())
                .status(StravaSyncStatus.APPLIED)
                .syncedAt(Instant.now())
                .build();

        bikeRepository.save(bike);
        activitySyncRepository.save(sync);

        return new StravaSyncResult(StravaSyncStatus.APPLIED, "Activity synced", activity.getId());
    }

    private boolean sameOriginalValues(StravaActivitySync existing, StravaActivity activity) {
        return safeLong(existing.getOriginalDistanceMeters()).equals(safeLong(activity.getDistanceMeters()))
                && safeLong(existing.getOriginalMovingTimeSeconds()).equals(safeLong(activity.getMovingTimeSeconds()));
    }

    private void updateSyncRecord(StravaActivitySync existing, StravaActivity activity, int distanceKm, int movingMinutes) {
        existing.setDistanceKmDelta(distanceKm);
        existing.setMovingTimeMinutesDelta(movingMinutes);
        existing.setOriginalDistanceMeters(activity.getDistanceMeters());
        existing.setOriginalMovingTimeSeconds(activity.getMovingTimeSeconds());
        existing.setSportType(activity.getSportType());
        existing.setStatus(StravaSyncStatus.APPLIED);
        existing.setSyncedAt(Instant.now());
    }

    private void applyBikeUsageDelta(Bike bike, int distanceDelta, int minutesDelta) {
        int currentOdometer = safeInt(bike.getOdometerKm());
        int currentUsageMinutes = safeInt(bike.getUsageTimeMinutes());
        bike.setOdometerKm(Math.max(0, currentOdometer + distanceDelta));
        bike.setUsageTimeMinutes(Math.max(0, currentUsageMinutes + minutesDelta));
    }

    private int toKilometers(Long distanceMeters) {
        if (distanceMeters == null) {
            return 0;
        }
        return BigDecimal.valueOf(distanceMeters)
                .divide(BigDecimal.valueOf(1000), 0, RoundingMode.HALF_UP)
                .intValue();
    }

    private int toMinutes(Long seconds) {
        if (seconds == null) {
            return 0;
        }
        return BigDecimal.valueOf(seconds)
                .divide(BigDecimal.valueOf(60), 0, RoundingMode.HALF_UP)
                .intValue();
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private Long safeLong(Long value) {
        return value == null ? 0L : value;
    }
}
