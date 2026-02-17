package com.quetoquenana.pedalpal.presentation.mapper;

import com.quetoquenana.pedalpal.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.application.command.CreateBikeResult;
import com.quetoquenana.pedalpal.domain.enums.BikeType;
import com.quetoquenana.pedalpal.presentation.dto.api.request.CreateBikeRequest;
import com.quetoquenana.pedalpal.presentation.dto.api.response.CreateBikeResponse;
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
}
