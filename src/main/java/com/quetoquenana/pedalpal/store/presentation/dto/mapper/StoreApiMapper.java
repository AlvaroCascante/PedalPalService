package com.quetoquenana.pedalpal.store.presentation.dto.mapper;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.store.application.result.StoreLocationResult;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;
import com.quetoquenana.pedalpal.store.presentation.dto.response.StoreLocationResponse;
import com.quetoquenana.pedalpal.store.presentation.dto.response.StoreResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StoreApiMapper {
    public StoreResponse toResponse(StoreResult result) {
        return this.toResponse(result,  Set.of(GeneralStatus.ACTIVE));
    }

    public StoreResponse toResponse(StoreResult result, Set<GeneralStatus> locationStatuses) {
        Set<StoreLocationResponse> locations = result.locations() == null
                ? Collections.emptySet() : result.locations()
                .stream()
                .filter(c -> locationStatuses.contains(c.status()))
                .map(this::toStoreLocationResponse)
                .collect(Collectors.toSet()
                );

        return new StoreResponse(
                result.id(),
                result.name(),
                locations
        );
    }

    private StoreLocationResponse toStoreLocationResponse(StoreLocationResult result) {
        return new StoreLocationResponse(
                result.id(),
                result.name(),
                result.website(),
                result.address(),
                result.latitude(),
                result.longitude(),
                result.phone(),
                result.status()
        );
    }
}
