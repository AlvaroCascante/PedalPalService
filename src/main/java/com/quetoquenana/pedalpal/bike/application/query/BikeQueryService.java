package com.quetoquenana.pedalpal.bike.application.query;

import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BikeQueryService {

    private final BikeMapper bikeMapper;
    private final BikeRepository bikeRepository;

    public BikeResult getById(UUID id, UUID ownerId) {
        Bike model = bikeRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(RecordNotFoundException::new);

        return bikeMapper.toBikeResult(model);
    }

    public List<BikeResult> findActiveByOwnerId(UUID ownerId) {
        List<Bike> models = bikeRepository.findByOwnerIdAndStatus(ownerId, BikeStatus.ACTIVE);

        return models.stream()
                .map(bikeMapper::toBikeResult)
                .toList();
    }
}
