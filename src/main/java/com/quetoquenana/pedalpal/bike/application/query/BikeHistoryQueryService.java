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

    private final BikeMapper bikeMapper;
    private final BikeRepository bikeRepository;
    private final BikeHistoryRepository bikeHistoryRepository;

    public BikeHistoryResult getById(UUID id) {
        BikeHistory bikeHistory = bikeHistoryRepository.getById(id)
                .orElseThrow(RecordNotFoundException::new);

        return bikeMapper.toBikeHistoryResult(bikeHistory);
    }

    public List<BikeHistoryResult> findByBikeId(UUID id, UUID ownerId) {
        Bike bike = bikeRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(RecordNotFoundException::new);

        List<BikeHistory> bikes = bikeHistoryRepository.findByBikeId(bike.getId());

        return bikes.stream()
                .map(bikeMapper::toBikeHistoryResult)
                .toList();
    }
}
