package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.RequestedService;
import com.quetoquenana.pedalpal.infrastructure.persistence.appointment.entity.AppointmentEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.appointment.entity.AppointmentServiceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AppointmentEntityMapper {

    public AppointmentEntity toEntity(Appointment model) {
        AppointmentEntity entity = AppointmentEntity.builder()
                .id(model.getId() == null ? UUID.randomUUID() : model.getId())
                .bikeId(model.getBikeId())
                .storeLocationId(model.getStoreLocationId())
                .scheduledAt(model.getScheduledAt())
                .status(model.getStatus())
                .notes(model.getNotes())
                .build();
        entity.setVersion(model.getVersion());
        model.getRequestedServices()
                .stream()
                .map(this::toEntity)
                .forEach(entity::addRequestedService);
        return entity;
    }

    private AppointmentServiceEntity toEntity(RequestedService model) {
        return AppointmentServiceEntity.builder()
                .id(model.getId())
                .productId(model.getProductId())
                .productNameSnapshot(model.getName())
                .priceSnapshot(model.getPrice())
                .build();
    }

    public Appointment toModel(AppointmentEntity entity) {
        List<RequestedService> requestedServices = entity.getServices() == null
                ? List.of()
                : entity.getServices().stream().map(this::toModel).toList();

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

    private RequestedService toModel(AppointmentServiceEntity entity) {
        return RequestedService.builder()
                .id(entity.getId())
                .name(entity.getProductNameSnapshot())
                .price(entity.getPriceSnapshot())
                .build();
    }
}
