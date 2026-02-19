package com.quetoquenana.pedalpal.presentation.mapper;

import com.quetoquenana.pedalpal.application.command.*;
import com.quetoquenana.pedalpal.application.result.BikeComponentResult;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.domain.enums.BikeComponentStatus;
import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.enums.BikeType;
import com.quetoquenana.pedalpal.presentation.dto.request.*;
import com.quetoquenana.pedalpal.presentation.dto.response.BikeComponentResponse;
import com.quetoquenana.pedalpal.presentation.dto.response.BikeResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BikeApiMapper {

    private final MessageSource messageSource;

    public BikeApiMapper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public CreateBikeCommand toCommand(
            UUID ownerId,
            CreateBikeRequest request
    )   {
        return CreateBikeCommand.builder()
                .ownerId(ownerId)
                .name(request.getName())
                .type(request.getType())
                .isPublic(request.isPublic())
                .isExternalSync(request.isExternalSync())
                .brand(request.getBrand())
                .model(request.getModel())
                .year(request.getYear())
                .serialNumber(request.getSerialNumber())
                .notes(request.getNotes())
                .odometerKm(request.getOdometerKm())
                .usageTimeMinutes(request.getUsageTimeMinutes())
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
                .name(request.getName())
                .type(request.getType())
                .isPublic(request.getIsPublic())
                .isExternalSync(request.getIsExternalSync())
                .brand(request.getBrand())
                .model(request.getModel())
                .year(request.getYear())
                .serialNumber(request.getSerialNumber())
                .notes(request.getNotes())
                .odometerKm(request.getOdometerKm())
                .usageTimeMinutes(request.getUsageTimeMinutes())
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
                .status(request.getStatus())
                .build();
    }

    public AddBikeComponentCommand toCommand(
            UUID bikeId,
            UUID authenticatedUserId,
            AddBikeComponentRequest request
    )   {
        return AddBikeComponentCommand.builder()
                .bikeId(bikeId)
                .authenticatedUserId(authenticatedUserId)
                .type(request.getType())
                .name(request.getName())
                .brand(request.getBrand())
                .model(request.getModel())
                .notes(request.getNotes())
                .odometerKm(request.getOdometerKm())
                .usageTimeMinutes(request.getUsageTimeMinutes())
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
                .type(request.getType())
                .name(request.getName())
                .brand(request.getBrand())
                .model(request.getModel())
                .notes(request.getNotes())
                .odometerKm(request.getOdometerKm())
                .usageTimeMinutes(request.getUsageTimeMinutes())
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
                .status(request.getStatus())
                .build();
    }

    public BikeResponse toResponse(BikeResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String typeLabel = messageSource.getMessage(BikeType.valueOf(result.type()).getKey(), null, locale);
        String statusLabel = messageSource.getMessage(BikeStatus.valueOf(result.status()).getKey(), null, locale);

        Set<BikeComponentResponse> components = result.components() == null
                ? Collections.emptySet()
                : result.components().stream().map(this::toComponentResponse).collect(Collectors.toSet());

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
        String statusLabel = messageSource.getMessage(BikeComponentStatus.valueOf(result.status()).getKey(), null, locale);

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
}
