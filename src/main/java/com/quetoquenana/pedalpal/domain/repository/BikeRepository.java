package com.quetoquenana.pedalpal.domain.repository;

import com.quetoquenana.pedalpal.domain.model.Bike;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BikeRepository {

    Optional<Bike> getById(UUID id);

    Bike save(Bike bike);

    void deleteById(UUID bikeId);

    List<Bike> findByOwnerId(UUID ownerId);

    List<Bike> findByOwnerIdAndStatusCodes(UUID ownerId, List<String> codes);

    boolean existsBySerialNumber(String serialNumber);

    Optional<Bike> findByIdAndOwnerId(UUID id, UUID ownerId);
}