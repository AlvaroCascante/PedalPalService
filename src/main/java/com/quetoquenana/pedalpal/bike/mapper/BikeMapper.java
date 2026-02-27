package com.quetoquenana.pedalpal.bike.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quetoquenana.pedalpal.bike.application.command.AddBikeComponentCommand;
import com.quetoquenana.pedalpal.bike.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.bike.application.result.BikeComponentResult;
import com.quetoquenana.pedalpal.bike.application.result.BikeHistoryResult;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.*;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BikeMapper {

    private final ObjectMapper objectMapper;

    public Bike toModel(CreateBikeCommand command) {
        return Bike.builder()
                .ownerId(command.ownerId())
                .name(command.name())
                .type(BikeType.from(command.type()))
                .status(BikeStatus.ACTIVE)
                .isPublic(command.isPublic())
                .isExternalSync(command.isExternalSync())
                .brand(command.brand())
                .model(command.model())
                .year(command.year())
                .serialNumber(command.serialNumber())
                .notes(command.notes())
                .odometerKm(command.odometerKm())
                .usageTimeMinutes(command.usageTimeMinutes())
                .build();
    }

    public BikeComponent toModel(
            AddBikeComponentCommand command,
            SystemCode componentType) {
        return BikeComponent.builder()
                .name(command.name())
                .componentType(componentType)
                .status(BikeComponentStatus.ACTIVE)
                .brand(command.brand())
                .model(command.model())
                .notes(command.notes())
                .odometerKm(command.odometerKm())
                .usageTimeMinutes(command.usageTimeMinutes())
                .build();
    }

    public BikeHistory toModel(BikeHistoryEvent event) {
        return BikeHistory.builder()
                .bikeId(event.bikeId())
                .performedBy(event.performedBy())
                .referenceId(event.referenceId())
                .type(event.bikeHistoryEventType())
                .payload(serializeChanges(event.changes()))
                .occurredAt(event.occurredAt())
                .build();
    }

    public BikeResult toResult(Bike model) {
        return new BikeResult(
                model.getId(),
                model.getName(),
                model.getType(),
                model.getStatus(),
                model.isPublic(),
                model.isExternalSync(),
                model.getBrand(),
                model.getModel(),
                model.getYear(),
                model.getSerialNumber(),
                model.getNotes(),
                model.getOdometerKm() == null ? 0 : model.getOdometerKm(),
                model.getUsageTimeMinutes() == null ? 0 : model.getUsageTimeMinutes(),
                model.getComponents().stream().map(this::toResult).collect(Collectors.toSet())
        );
    }

    public BikeComponentResult toResult(BikeComponent model) {
        return new BikeComponentResult(
                model.getId(),
                model.getComponentType(),
                model.getName(),
                model.getStatus(),
                model.getBrand(),
                model.getModel(),
                model.getNotes(),
                model.getOdometerKm() == null ? 0 : model.getOdometerKm(),
                model.getUsageTimeMinutes() == null ? 0 : model.getUsageTimeMinutes()
        );
    }

    public BikeHistoryResult toResult(BikeHistory model) {
        return new BikeHistoryResult(
                model.getId(),
                model.getId(),
                model.getOccurredAt(),
                model.getPerformedBy(),
                model.getType(),
                model.getPayload()
        );
    }

    private String serializeChanges(List<BikeChangeItem> changes) {
        try {
            return objectMapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize bike history changes", e);
        }
    }
}
