package com.quetoquenana.pedalpal.bike.application.query;

import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeHistoryResult;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistory;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeHistoryRepository;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BikeHistoryQueryService {


    private final AuthenticatedUserPort authenticatedUserPort;
    private final BikeMapper mapper;
    private final BikeRepository repository;
    private final BikeHistoryRepository bikeHistoryRepository;

    public BikeHistoryResult getById(UUID id) {
        BikeHistory response = bikeHistoryRepository.getById(id)
                .orElseThrow(RecordNotFoundException::new);

        return mapper.toResult(response);
    }

    public List<BikeHistoryResult> findByBikeId(UUID id) {
        Bike bike = repository.findByIdAndOwnerId(id, getAuthenticatedUserId())
                .orElseThrow(RecordNotFoundException::new);

        List<BikeHistory> response = bikeHistoryRepository.findByBikeId(bike.getId());

        return response.stream()
                .map(mapper::toResult)
                .toList();
    }

    private UUID getAuthenticatedUserId() {
        return authenticatedUserPort.getAuthenticatedUser()
                .map(AuthenticatedUser::userId)
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));
    }
}
