package com.quetoquenana.pedalpal.store.mapper;

import com.quetoquenana.pedalpal.store.application.result.StoreLocationResult;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;
import com.quetoquenana.pedalpal.store.domain.model.Store;
import com.quetoquenana.pedalpal.store.domain.model.StoreLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StoreMapper {

    public StoreResult toResult(Store model) {
        return new StoreResult(
                model.getId(),
                model.getName(),
                model.getLocations()
                        .stream()
                        .map(this::toResult)
                        .collect(Collectors.toSet())
        );
    }

    private StoreLocationResult toResult(StoreLocation model) {
        return new StoreLocationResult(
                model.getId(),
                model.getName(),
                model.getWebsite(),
                model.getAddress(),
                model.getLatitude(),
                model.getLongitude(),
                model.getPhone(),
                model.getTimezone(),
                model.getStatus()
        );
    }
}
