package com.quetoquenana.pedalpal.bike.application.query;

import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BikeQueryService {

    private final AuthenticatedUserPort authenticatedUserPort;
    private final BikeMapper mapper;
    private final BikeRepository repository;

    public BikeResult getById(UUID id) {


        Bike model = repository.findByIdAndOwnerId(id, getAuthenticatedUserId())
                .orElseThrow(RecordNotFoundException::new);

        return mapper.toResult(model);
    }

    public List<BikeResult> findActiveByOwnerId() {
        List<Bike> models = repository.findByOwnerIdAndStatus(getAuthenticatedUserId(), BikeStatus.ACTIVE);

        return models.stream()
                .map(mapper::toResult)
                .toList();
    }

    private UUID getAuthenticatedUserId() {
        return authenticatedUserPort.getAuthenticatedUser()
                .map(AuthenticatedUser::userId)
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));
    }
}
