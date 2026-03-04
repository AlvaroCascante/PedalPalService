package com.quetoquenana.pedalpal.serviceOrder.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.serviceOrder.infrastructure.persistence.entity.ServiceOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ServiceOrderJpaRepository extends JpaRepository<ServiceOrderEntity, UUID> {
    Optional<ServiceOrderEntity> getByAppointmentId(UUID appointmentId);

    @Query(value = "SELECT nextval('service_order_number_seq')", nativeQuery = true)
    long getNextServiceOrderNumber();
}

