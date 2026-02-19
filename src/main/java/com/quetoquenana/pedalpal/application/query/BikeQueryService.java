package com.quetoquenana.pedalpal.application.query;

import com.quetoquenana.pedalpal.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BikeQueryService {
    private final BikeRepository bikeRepository;

    public BikeResult getById(UUID id, UUID ownerId) {
        Bike bike = bikeRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(RecordNotFoundException::new);

        return BikeMapper.toBikeResult(bike);
    }

    public List<BikeResult> fetchActiveByOwnerId(UUID ownerId) {
        List<Bike> bikes = bikeRepository.findByOwnerIdAndStatus(ownerId, BikeStatus.ACTIVE.name());

        return bikes.stream()
                .map(BikeMapper::toBikeResult)
                .toList();
    }
}
