package com.quetoquenana.pedalpal.bike.application.query;

import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeHistoryResult;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistory;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeHistoryRepository;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BikeHistoryQueryService {

    private final BikeMapper mapper;
    private final BikeRepository repository;
    private final BikeHistoryRepository bikeHistoryRepository;

    public BikeHistoryResult getById(UUID id) {
        BikeHistory bikeHistory = bikeHistoryRepository.getById(id)
                .orElseThrow(RecordNotFoundException::new);

        return mapper.toBikeHistoryResult(bikeHistory);
    }

    public List<BikeHistoryResult> findByBikeId(UUID id, UUID ownerId) {
        Bike bike = repository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(RecordNotFoundException::new);

        List<BikeHistory> bikes = bikeHistoryRepository.findByBikeId(bike.getId());

        return bikes.stream()
                .map(mapper::toBikeHistoryResult)
                .toList();
    }
}
