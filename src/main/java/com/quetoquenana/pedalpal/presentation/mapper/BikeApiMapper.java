package com.quetoquenana.pedalpal.presentation.mapper;

import com.quetoquenana.pedalpal.application.command.*;
import com.quetoquenana.pedalpal.domain.enums.BikeType;
import com.quetoquenana.pedalpal.presentation.dto.api.request.CreateBikeRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.request.UpdateBikeRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.request.UpdateBikeStatusRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.response.CreateBikeResponse;
import com.quetoquenana.pedalpal.presentation.dto.api.response.UpdateBikeResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

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

    public CreateBikeResponse toResponse(
            CreateBikeResult result
    ) {
        Locale locale = LocaleContextHolder.getLocale();
        String typeLabel = messageSource.getMessage(BikeType.valueOf(result.type()).getKey(), null, locale);

        return new CreateBikeResponse(
                result.id(),
                result.name(),
                typeLabel,
                result.isPublic(),
                result.isExternalSync(),
                result.brand(),
                result.model(),
                result.year(),
                result.serialNumber(),
                result.notes(),
                result.odometerKm(),
                result.usageTimeMinutes()
        );
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

    public UpdateBikeResponse toResponse(UpdateBikeResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String typeLabel = messageSource.getMessage(BikeType.valueOf(result.type()).getKey(), null, locale);

        return new UpdateBikeResponse(
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
                result.usageTimeMinutes()
        );
    }

    public UpdateBikeStatusCommand toCommand(UpdateBikeStatusRequest request, UUID bikeId, UUID authenticatedUserId) {
        return new UpdateBikeStatusCommand(
                bikeId,
                authenticatedUserId,
                request.getStatus()
        );
    }
}
