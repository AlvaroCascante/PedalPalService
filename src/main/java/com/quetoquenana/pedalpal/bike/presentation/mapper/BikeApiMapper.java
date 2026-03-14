package com.quetoquenana.pedalpal.bike.presentation.mapper;

import com.quetoquenana.pedalpal.bike.application.command.*;
import com.quetoquenana.pedalpal.bike.application.result.BikeComponentResult;
import com.quetoquenana.pedalpal.bike.application.result.BikeHistoryResult;
import com.quetoquenana.pedalpal.bike.application.result.BikeMediaResult;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.BikeComponentStatus;
import com.quetoquenana.pedalpal.bike.presentation.dto.request.*;
import com.quetoquenana.pedalpal.bike.presentation.dto.response.BikeComponentResponse;
import com.quetoquenana.pedalpal.bike.presentation.dto.response.BikeHistoryResponse;
import com.quetoquenana.pedalpal.bike.presentation.dto.response.BikeMediaResponse;
import com.quetoquenana.pedalpal.bike.presentation.dto.response.BikeResponse;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.media.presentation.dto.response.MediaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BikeApiMapper {

    private final MessageSource messageSource;

    public CreateBikeCommand toCommand(
            CreateBikeRequest request
    )   {
        return new CreateBikeCommand(
                request.name(),
                request.type(),
                request.brand(),
                request.model(),
                request.year(),
                request.serialNumber(),
                request.notes(),
                request.odometerKm(),
                request.usageTimeMinutes(),
                request.externalGearId(),
                request.externalSyncProvider(),
                request.isPublic(),
                request.isExternalSync()
        );
    }

    public UpdateBikeCommand toCommand(
            UUID bikeId,
            UpdateBikeRequest request
    ) {
        return new UpdateBikeCommand(
                bikeId,
                request.name(),
                request.type(),
                request.brand(),
                request.model(),
                request.year(),
                request.serialNumber(),
                request.notes(),
                request.odometerKm(),
                request.usageTimeMinutes(),
                request.isPublic(),
                request.isExternalSync(),
                request.externalGearId(),
                request.externalSyncProvider()
        );
    }

    public UpdateBikeStatusCommand toCommand(
            UUID bikeId,
            UpdateBikeStatusRequest request
    ) {
        return new UpdateBikeStatusCommand(
                bikeId,
                request.status()
        );
    }

    public AddBikeComponentCommand toCommand(
            UUID bikeId,
            UUID componentId,
            AddBikeComponentRequest request
    )   {
        return new AddBikeComponentCommand(
                componentId,
                bikeId,
                request.type(),
                request.name(),
                request.brand(),
                request.model(),
                request.notes(),
                request.odometerKm(),
                request.usageTimeMinutes()
        );
    }

    public UpdateBikeComponentCommand toCommand(
            UUID bikeId,
            UUID componentId,
            UpdateBikeComponentRequest request
    ) {
        return new UpdateBikeComponentCommand(
                bikeId,
                componentId,
                request.type(),
                request.name(),
                request.brand(),
                request.model(),
                request.notes(),
                request.odometerKm(),
                request.usageTimeMinutes()
        );
    }

    public UpdateBikeComponentStatusCommand toCommand(
            UUID bikeId,
            UUID componentId,
            UpdateBikeComponentStatusRequest request
    ) {
        return new UpdateBikeComponentStatusCommand(
                bikeId,
                componentId,
                request.status()
        );
    }

    public CreateBikeUploadMediaCommand toCommand(
            UUID bikeId,
            UploadBikeMediaRequest request
    ) {
        return new CreateBikeUploadMediaCommand(
                bikeId,
                request.mediaFiles()
                        .stream()
                        .map(this::toCommand)
                        .toList()
        );
    }

    private BikeMediaCommand toCommand(BikeMediaRequest request) {
        return new BikeMediaCommand(
                request.contentType(),
                request.isPrimary(),
                request.name(),
                request.altText()
        );
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
                .map(this::toResponse)
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
                result.externalGearId(),
                result.externalSyncProvider(),
                components
        );
    }

    private BikeComponentResponse toResponse(BikeComponentResult result) {
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
        Locale locale = LocaleContextHolder.getLocale();
        String typeLabel = messageSource.getMessage(result.type().getKey(), null, locale);

        return new BikeHistoryResponse(
                result.id(),
                result.bikeId(),
                result.occurredAt(),
                result.performedBy(),
                typeLabel,
                result.payload()
        );
    }

    public BikeMediaResponse toResponse(BikeMediaResult result) {
        return new BikeMediaResponse(
                result.id(),
                result.mediaResults().stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    private MediaResponse toResponse(MediaResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        return new MediaResponse(
                result.id(),
                result.contentType(),
                result.provider(),
                result.isPrimary(),
                statusLabel,
                result.name(),
                result.altText(),
                result.url(),
                result.expiresAt()
        );
    }
}
