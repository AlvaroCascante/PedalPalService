package com.quetoquenana.pedalpal.serviceOrder.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrderDetail;
import com.quetoquenana.pedalpal.serviceOrder.infrastructure.persistence.entity.ServiceOrderDetailEntity;
import com.quetoquenana.pedalpal.serviceOrder.infrastructure.persistence.entity.ServiceOrderEntity;

/**
 * Maps ServiceOrder persistence entities to domain models and back.
 * Prefer static utility if they are pure and dependency‑free.
 * If they need JPA helpers, converters, or other collaborators,
 * use DI and keep them package‑private when possible.
 */
public class ServiceOrderEntityMapper {

    private ServiceOrderEntityMapper() {}

    public static ServiceOrder toModel(ServiceOrderEntity entity) {
        ServiceOrder model = ServiceOrder.builder()
                .id(entity.getId())
                .appointmentId(entity.getAppointmentId())
                .bikeId(entity.getBikeId())
                .startedAt(entity.getStartedAt())
                .completedAt(entity.getCompletedAt())
                .totalPrice(entity.getTotalPrice())
                .status(entity.getStatus())
                .orderNumber(entity.getOrderNumber())
                .notes(entity.getNotes())
                .build();
        model.setVersion(entity.getVersion());

        if (entity.getServiceOrderDetails() != null) {
            entity.getServiceOrderDetails()
                    .stream()
                    .map(ServiceOrderEntityMapper::toModel)
                    .forEach(model::addRequestedService);
        }
        return model;
    }

    private static ServiceOrderDetail toModel(ServiceOrderDetailEntity entity) {
        return ServiceOrderDetail.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .technicianId(entity.getTechnicianId())
                .productNameSnapshot(entity.getProductNameSnapshot())
                .priceSnapshot(entity.getPriceSnapshot())
                .status(entity.getStatus())
                .startedAt(entity.getStartedAt())
                .completedAt(entity.getCompletedAt())
                .notes(entity.getNotes())
                .build();
    }

    public static ServiceOrderEntity toEntity(ServiceOrder model) {
        ServiceOrderEntity entity = ServiceOrderEntity.builder()
                .id(model.getId())
                .appointmentId(model.getAppointmentId())
                .bikeId(model.getBikeId())
                .status(model.getStatus())
                .startedAt(model.getStartedAt())
                .completedAt(model.getCompletedAt())
                .totalPrice(model.getTotalPrice())
                .orderNumber(model.getOrderNumber())
                .build();
        entity.setVersion(model.getVersion());
        model.getRequestedServices()
                .stream()
                .map(ServiceOrderEntityMapper::toEntity)
                .forEach(entity::addServiceOrderDetail);
        return entity;
    }

    private static ServiceOrderDetailEntity toEntity(ServiceOrderDetail model) {
        return ServiceOrderDetailEntity.builder()
                .id(model.getId())
                .productId(model.getProductId())
                .technicianId(model.getTechnicianId())
                .productNameSnapshot(model.getProductNameSnapshot())
                .priceSnapshot(model.getPriceSnapshot())
                .status(model.getStatus())
                .startedAt(model.getStartedAt())
                .completedAt(model.getCompletedAt())
                .notes(model.getNotes())
                .build();
    }
}
