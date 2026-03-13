package com.quetoquenana.pedalpal.strava.application.service;

import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.ExternalSyncProvider;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Application service for mapping Strava gear IDs to bikes.
 */
public class StravaBikeMatchingService {

    private final BikeRepository bikeRepository;

    public StravaBikeMatchingService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    /**
     * Finds a bike by Strava gear ID and validates external sync configuration.
     */
    public Optional<Bike> findBike(UUID ownerId, String gearId) {
        if (ownerId == null || gearId == null || gearId.isBlank()) {
            return Optional.empty();
        }
        return bikeRepository.findByOwnerIdAndExternalGearId(ownerId, gearId, ExternalSyncProvider.STRAVA);
    }
}
