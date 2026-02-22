package com.quetoquenana.pedalpal.bike.domain.repository;

import com.quetoquenana.pedalpal.bike.domain.model.BikeHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BikeHistoryRepository {

    Optional<BikeHistory> getById(UUID id);

    BikeHistory save(BikeHistory bikeHistory);

    List<BikeHistory> findByBikeId(UUID bikeId);
}