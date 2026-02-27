package com.quetoquenana.pedalpal.infrastructure.persistence.serviceOrder.repository;

import com.quetoquenana.pedalpal.infrastructure.persistence.serviceOrder.entity.ServiceOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceOrderJpaRepository extends JpaRepository<ServiceOrderEntity, UUID> {
    Optional<ServiceOrderEntity> getByAppointmentId(UUID appointmentId);
}

