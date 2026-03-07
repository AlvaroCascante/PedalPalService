package com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.entity.ServiceOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceOrderJpaRepository extends JpaRepository<ServiceOrderEntity, UUID> {
    Optional<ServiceOrderEntity> findByAppointmentId(UUID appointmentId);

    List<ServiceOrderEntity> findByBikeId(UUID bikeId);

    @Query(value = "SELECT nextval('service_order_number_seq')", nativeQuery = true)
    long nextOrderSequence();
}
