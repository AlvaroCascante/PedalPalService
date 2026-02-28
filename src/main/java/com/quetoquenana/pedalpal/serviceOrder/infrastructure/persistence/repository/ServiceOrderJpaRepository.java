package com.quetoquenana.pedalpal.serviceOrder.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.serviceOrder.infrastructure.persistence.entity.ServiceOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceOrderJpaRepository extends JpaRepository<ServiceOrderEntity, UUID> {
    Optional<ServiceOrderEntity> getByAppointmentId(UUID appointmentId);
}

