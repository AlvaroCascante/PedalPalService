package com.quetoquenana.pedalpal.bike.presentation.mapper;

import com.quetoquenana.pedalpal.bike.application.command.*;
import com.quetoquenana.pedalpal.bike.application.result.BikeComponentResult;
import com.quetoquenana.pedalpal.bike.application.result.BikeHistoryResult;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.BikeComponentStatus;
import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.model.BikeType;
import com.quetoquenana.pedalpal.bike.presentation.dto.request.*;
import com.quetoquenana.pedalpal.bike.presentation.dto.response.BikeComponentResponse;
import com.quetoquenana.pedalpal.bike.presentation.dto.response.BikeHistoryResponse;
import com.quetoquenana.pedalpal.bike.presentation.dto.response.BikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BikeApiMapper {

    private final MessageSource messageSource;

    public CreateBikeCommand toCommand(
            UUID ownerId,
            CreateBikeRequest request
    )   {
        return CreateBikeCommand.builder()
                .ownerId(ownerId)
                .name(request.name())
                .type(request.type())
                .isPublic(request.isPublic())
                .isExternalSync(request.isExternalSync())
                .brand(request.brand())
                .model(request.model())
                .year(request.year())
                .serialNumber(request.serialNumber())
                .notes(request.notes())
                .odometerKm(request.odometerKm())
                .usageTimeMinutes(request.usageTimeMinutes())
                .build();
    }

    public UpdateBikeCommand toCommand(
            UUID bikeId,
            UUID authenticatedUserId,
            UpdateBikeRequest request
    ) {
        return UpdateBikeCommand.builder()
                .bikeId(bikeId)
                .authenticatedUserId(authenticatedUserId)
                .name(request.name())
                .type(request.type())
                .isPublic(request.isPublic())
                .isExternalSync(request.isExternalSync())
                .brand(request.brand())
                .model(request.model())
                .year(request.year())
                .serialNumber(request.serialNumber())
                .notes(request.notes())
                .odometerKm(request.odometerKm())
                .usageTimeMinutes(request.usageTimeMinutes())
                .build();
    }

    public UpdateBikeStatusCommand toCommand(
            UUID bikeId,
            UUID authenticatedUserId,
            UpdateBikeStatusRequest request
    ) {
        return UpdateBikeStatusCommand.builder()
                .bikeId(bikeId)
                .authenticatedUserId(authenticatedUserId)
                .status(request.status())
                .build();
    }

    public AddBikeComponentCommand toCommand(
            UUID bikeId,
            UUID componentId,
            UUID authenticatedUserId,
            AddBikeComponentRequest request
    )   {
        return AddBikeComponentCommand.builder()
                .bikeId(bikeId)
                .componentId(componentId)
                .authenticatedUserId(authenticatedUserId)
                .type(request.type())
                .name(request.name())
                .brand(request.brand())
                .model(request.model())
                .notes(request.notes())
                .odometerKm(request.odometerKm())
                .usageTimeMinutes(request.usageTimeMinutes())
                .build();
    }

    public UpdateBikeComponentCommand toCommand(
            UUID bikeId,
            UUID componentId,
            UUID authenticatedUserId,
            UpdateBikeComponentRequest request
    ) {
        return UpdateBikeComponentCommand.builder()
                .bikeId(bikeId)
                .componentId(componentId)
                .authenticatedUserId(authenticatedUserId)
                .type(request.type())
                .name(request.name())
                .brand(request.brand())
                .model(request.model())
                .notes(request.notes())
                .odometerKm(request.odometerKm())
                .usageTimeMinutes(request.usageTimeMinutes())
                .build();
    }

    public UpdateBikeComponentStatusCommand toCommand(
            UUID bikeId,
            UUID componentId,
            UUID authenticatedUserId,
            UpdateBikeComponentStatusRequest request
    ) {
        return UpdateBikeComponentStatusCommand.builder()
                .bikeId(bikeId)
                .componentId(componentId)
                .authenticatedUserId(authenticatedUserId)
                .status(request.status())
                .build();
    }

    public BikeResponse toResponse(BikeResult result) {
        return toResponse(result, Set.of(BikeComponentStatus.ACTIVE));
    }

    public BikeResponse toResponse(BikeResult result, Set<BikeComponentStatus> componentStatuses) {
        Locale locale = LocaleContextHolder.getLocale();
        String typeLabel = messageSource.getMessage(result.type().getKey(), null, locale);
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        Set<BikeComponentResponse> components = result.components() == null
                ? Collections.emptySet() : result.getComponents(componentStatuses == null ? Set.of(BikeComponentStatus.ACTIVE) : componentStatuses)
                .stream()
                .map(this::toComponentResponse)
                .collect(Collectors.toSet()
                );

        return new BikeResponse(
                result.id(),
                result.name(),
                typeLabel,
                statusLabel,
                result.isPublic(),
                result.isExternalSync(),
                result.brand(),
                result.model(),
                result.year(),
                result.serialNumber(),
                result.notes(),
                result.odometerKm(),
                result.usageTimeMinutes(),
                components
        );
    }

    private BikeComponentResponse toComponentResponse(BikeComponentResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String typeLabel = messageSource.getMessage(result.type().getCodeKey(), null, locale);
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        return new BikeComponentResponse(
                result.id(),
                typeLabel,
                result.name(),
                statusLabel,
                result.brand(),
                result.model(),
                result.notes(),
                result.odometerKm(),
                result.usageTimeMinutes()
        );
    }

    public BikeHistoryResponse toResponse(BikeHistoryResult result) {
        return new BikeHistoryResponse(
                result.id(),
                result.bikeId(),
                result.occurredAt(),
                result.performedBy(),
                result.type(),
                result.payload()
        );
    }
}
