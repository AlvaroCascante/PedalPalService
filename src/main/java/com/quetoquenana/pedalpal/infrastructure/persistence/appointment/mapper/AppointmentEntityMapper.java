package com.quetoquenana.pedalpal.infrastructure.persistence.appointment.mapper;

import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentService;
import com.quetoquenana.pedalpal.infrastructure.persistence.appointment.entity.AppointmentEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.appointment.entity.AppointmentServiceEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductEntity;
import com.quetoquenana.pedalpal.product.domain.model.Product;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class AppointmentEntityMapper {

    public AppointmentEntity toEntity(Appointment domain) {
        AppointmentEntity entity = AppointmentEntity.builder()
                .id(domain.getId() == null ? UUID.randomUUID() : domain.getId())
                .bikeId(domain.getBikeId())
                .storeLocationId(domain.getStoreLocationId())
                .scheduledAt(domain.getScheduledAt())
                .status(domain.getStatus())
                .notes(domain.getNotes())
                .build();
        entity.setVersion(domain.getVersion());

        Set<AppointmentServiceEntity> rsEntities = new HashSet<>();
        for (AppointmentService rs : domain.getRequestedServices()) {
            rsEntities.add(AppointmentServiceEntity.builder()
                    .id(rs.getId() == null ? UUID.randomUUID() : rs.getId())
                    .appointment(entity)
                    .product(toProductEntity(rs.getProduct()))
                    .productNameSnapshot(rs.getProductNameSnapshot())
                    .priceSnapshot(rs.getPriceSnapshot())
                    .build());
        }
        entity.setServices(rsEntities);
        return entity;
    }

    public Appointment toDomain(AppointmentEntity entity) {
        List<AppointmentService> requestedServices = entity.getServices() == null
                ? List.of()
                : entity.getServices().stream().map(this::toDomainRequestedService).toList();

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

    public void updateEntity(AppointmentEntity entity, Appointment domain) {
        entity.setBikeId(domain.getBikeId());
        entity.setStoreLocationId(domain.getStoreLocationId());
        entity.setScheduledAt(domain.getScheduledAt());
        entity.setStatus(domain.getStatus());
        entity.setNotes(domain.getNotes());
        entity.setVersion(domain.getVersion());

        entity.getServices().clear();
        for (AppointmentService rs : domain.getRequestedServices()) {
            entity.getServices().add(AppointmentServiceEntity.builder()
                    .id(rs.getId() == null ? UUID.randomUUID() : rs.getId())
                    .appointment(entity)
                    .product(toProductEntity(rs.getProduct()))
                    .productNameSnapshot(rs.getProductNameSnapshot())
                    .priceSnapshot(rs.getPriceSnapshot())
                    .build());
        }
    }

    private AppointmentService toDomainRequestedService(AppointmentServiceEntity e) {
        return AppointmentService.builder()
                .id(e.getId())
                .product(toProduct(e.getProduct()))
                .productNameSnapshot(e.getProductNameSnapshot())
                .priceSnapshot(e.getPriceSnapshot())
                .build();
    }

    private Product toProduct(ProductEntity e) {
        return Product.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .price(e.getPrice())
                .status(e.getStatus())
                .build();
    }

    private ProductEntity toProductEntity(Product p) {
        return ProductEntity.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .status(p.getStatus())
                .build();
    }
}
