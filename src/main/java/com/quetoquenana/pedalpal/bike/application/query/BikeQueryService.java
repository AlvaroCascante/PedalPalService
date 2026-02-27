package com.quetoquenana.pedalpal.bike.application.query;

import com.quetoquenana.pedalpal.bike.mapper.BikeMapper;
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

    private final BikeMapper mapper;
    private final BikeRepository repository;

    public BikeResult getById(UUID id, UUID ownerId) {
        Bike model = repository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(RecordNotFoundException::new);

        return mapper.toResult(model);
    }

    public List<BikeResult> findActiveByOwnerId(UUID ownerId) {
        List<Bike> models = repository.findByOwnerIdAndStatus(ownerId, BikeStatus.ACTIVE);

        return models.stream()
                .map(mapper::toResult)
                .toList();
    }
}
