package com.quetoquenana.pedalpal.presentation.mapper;

import com.quetoquenana.pedalpal.application.command.*;
import com.quetoquenana.pedalpal.application.result.BikeComponentResult;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.domain.enums.BikeType;
import com.quetoquenana.pedalpal.presentation.dto.api.request.AddBikeComponentRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.request.CreateBikeRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.request.UpdateBikeRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.request.UpdateBikeStatusRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.response.BikeComponentResponse;
import com.quetoquenana.pedalpal.presentation.dto.api.response.BikeResponse;
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
            CreateBikeRequest request,
            UUID ownerId
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

    public UpdateBikeCommand toCommand(UpdateBikeRequest request, UUID bikeId, UUID authenticatedUserId) {
        return new UpdateBikeCommand(
                bikeId,
                authenticatedUserId,
                request.getName(),
                request.getType(),
                request.getBrand(),
                request.getModel(),
                request.getYear(),
                request.getSerialNumber(),
                request.getNotes(),
                request.getOdometerKm(),
                request.getUsageTimeMinutes(),
                request.getIsPublic(),
                request.getIsExternalSync()
        );
    }

    public BikeResponse toResponse(BikeResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String typeLabel = messageSource.getMessage(BikeType.valueOf(result.type()).getKey(), null, locale);

        Set<BikeComponentResponse> components = result.components() == null
                ? Collections.emptySet()
                : result.components().stream().map(this::toComponentResponse).collect(Collectors.toSet());

        return new BikeResponse(
                result.id(),
                result.name(),
                typeLabel,
                result.status(),
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

    private BikeComponentResponse toComponentResponse(BikeComponentResult component) {
        return new BikeComponentResponse(
                component.id(),
                component.type(),
                component.name(),
                component.brand(),
                component.model(),
                component.notes(),
                component.odometerKm(),
                component.usageTimeMinutes()
        );
    }

    public UpdateBikeStatusCommand toCommand(UpdateBikeStatusRequest request, UUID bikeId, UUID authenticatedUserId) {
        return new UpdateBikeStatusCommand(
                bikeId,
                authenticatedUserId,
                request.getStatus()
        );
    }

    public AddBikeComponentCommand toCommand(
            UUID bikeId,
            UUID ownerId,
            AddBikeComponentRequest request
    )   {
        return AddBikeComponentCommand.builder()
                .bikeId(bikeId)
                .authenticatedUserId(ownerId)
                .type(request.getType())
                .name(request.getName())
                .brand(request.getBrand())
                .model(request.getModel())
                .notes(request.getNotes())
                .odometerKm(request.getOdometerKm())
                .usageTimeMinutes(request.getUsageTimeMinutes())
                .build();
    }
}
