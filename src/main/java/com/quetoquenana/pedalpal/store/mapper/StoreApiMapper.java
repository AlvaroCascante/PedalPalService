package com.quetoquenana.pedalpal.store.mapper;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.store.application.result.StoreLocationResult;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;
import com.quetoquenana.pedalpal.store.presentation.dto.response.StoreLocationResponse;
import com.quetoquenana.pedalpal.store.presentation.dto.response.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StoreApiMapper {

    private final MessageSource messageSource;

    public StoreResponse toResponse(StoreResult result) {
        return this.toResponse(result,  Set.of(GeneralStatus.ACTIVE));
    }

    public StoreResponse toResponse(StoreResult result, Set<GeneralStatus> locationStatuses) {
        if (locationStatuses == null) {
            return this.toResponse(result);
        }

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
        Locale locale = LocaleContextHolder.getLocale();
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        return new StoreLocationResponse(
                result.id(),
                result.name(),
                result.website(),
                result.address(),
                result.latitude(),
                result.longitude(),
                result.phone(),
                result.timezone(),
                statusLabel
        );
    }
}
