package com.quetoquenana.pedalpal.bike.domain.repository;

import com.quetoquenana.pedalpal.bike.domain.model.BikeComponent;

import java.util.Optional;
import java.util.UUID;

public interface BikeComponentRepository {

    Optional<BikeComponent> getById(UUID bikeComponentId);

    BikeComponent save(BikeComponent bikeComponent);

    BikeComponent update(UUID bikeComponentId, BikeComponent bikeComponent);

    void deleteById(UUID bikeComponentId);
}

