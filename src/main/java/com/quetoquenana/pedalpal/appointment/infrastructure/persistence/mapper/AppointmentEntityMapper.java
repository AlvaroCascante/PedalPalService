package com.quetoquenana.pedalpal.appointment.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.RequestedService;
import com.quetoquenana.pedalpal.appointment.infrastructure.persistence.entity.AppointmentEntity;
import com.quetoquenana.pedalpal.appointment.infrastructure.persistence.entity.AppointmentServiceEntity;

import java.util.List;

/**
 * Maps appointment persistence entities to domain models and back.
 * Prefer static utility if they are pure and dependency‑free.
 * If they need JPA helpers, converters, or other collaborators,
 * use DI and keep them package‑private when possible.
 */
public class AppointmentEntityMapper {

    private AppointmentEntityMapper() {}

    public static AppointmentEntity toEntity(Appointment model) {
        AppointmentEntity entity = AppointmentEntity.builder()
                .id(model.getId())
                .bikeId(model.getBikeId())
                .storeLocationId(model.getStoreLocationId())
                .scheduledAt(model.getScheduledAt())
                .status(model.getStatus())
                .notes(model.getNotes())
                .build();
        entity.setVersion(model.getVersion());
        model.getRequestedServices()
                .stream()
                .map(AppointmentEntityMapper::toEntity)
                .forEach(entity::addRequestedService);
        return entity;
    }

    private static AppointmentServiceEntity toEntity(RequestedService model) {
        return AppointmentServiceEntity.builder()
                .id(model.getId())
                .productId(model.getServiceId())
                .productNameSnapshot(model.getName())
                .priceSnapshot(model.getPrice())
                .build();
    }

    public static Appointment toModel(AppointmentEntity entity) {
        List<RequestedService> requestedServices = entity.getServices() == null
                ? List.of()
                : entity.getServices().stream().map(AppointmentEntityMapper::toModel).toList();

        Appointment domain = Appointment.builder()
                .id(entity.getId())
                .bikeId(entity.getBikeId())
                .storeLocationId(entity.getStoreLocationId())
                .scheduledAt(entity.getScheduledAt())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .requestedServices(requestedServices)
                .build();

        domain.setVersion(entity.getVersion());
        return domain;
    }

    private static RequestedService toModel(AppointmentServiceEntity entity) {
        return RequestedService.builder()
                .id(entity.getId())
                .serviceId(entity.getProductId())
                .name(entity.getProductNameSnapshot())
                .price(entity.getPriceSnapshot())
                .build();
    }
}
