package com.quetoquenana.pedalpal.bike.domain.repository;

import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BikeRepository {

    Optional<Bike> getById(UUID id);

    Bike save(Bike bike);

    void deleteById(UUID bikeId);

    boolean existsBySerialNumber(String serialNumber);

    Optional<Bike> findByIdAndOwnerId(UUID id, UUID ownerId);

    List<Bike> findByOwnerIdAndStatus(UUID ownerId, BikeStatus bikeStatus);
}