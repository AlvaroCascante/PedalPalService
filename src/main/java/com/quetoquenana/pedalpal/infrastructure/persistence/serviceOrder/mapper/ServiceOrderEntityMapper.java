package com.quetoquenana.pedalpal.infrastructure.persistence.serviceOrder.mapper;

import com.quetoquenana.pedalpal.infrastructure.persistence.serviceOrder.entity.ServiceOrderDetailEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.serviceOrder.entity.ServiceOrderEntity;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceOrderEntityMapper {

    public ServiceOrder toModel(ServiceOrderEntity entity) {
        ServiceOrder model = ServiceOrder.builder()
                .id(entity.getId())
                .appointmentId(entity.getAppointmentId())
                .bikeId(entity.getBikeId())
                .startedAt(entity.getStartedAt())
                .completedAt(entity.getCompletedAt())
                .totalPrice(entity.getTotalPrice())
                .status(entity.getStatus())
                .build();
        model.setVersion(entity.getVersion());

        if (entity.getServiceOrderDetails() != null) {
            entity.getServiceOrderDetails()
                    .stream()
                    .map(this::toModel)
                    .forEach(model::addRequestedService);
        }
        return model;
    }

    private ServiceOrderDetail toModel(ServiceOrderDetailEntity entity) {
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

    public ServiceOrderEntity toEntity(ServiceOrder model) {
        ServiceOrderEntity entity = ServiceOrderEntity.builder()
                .id(model.getId())
                .appointmentId(model.getAppointmentId())
                .bikeId(model.getBikeId())
                .status(model.getStatus())
                .startedAt(model.getStartedAt())
                .completedAt(model.getCompletedAt())
                .totalPrice(model.getTotalPrice())
                .build();
        entity.setVersion(model.getVersion());
        model.getRequestedServices()
                .stream()
                .map(this::toEntity)
                .forEach(entity::addServiceOrderDetail);
        return entity;
    }

    private ServiceOrderDetailEntity toEntity(ServiceOrderDetail model) {
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
