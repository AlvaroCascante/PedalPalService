package com.quetoquenana.pedalpal.serviceorder.domain.repository;

import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceOrderRepository {
    ServiceOrder save(ServiceOrder model);

    Optional<ServiceOrder> getById(UUID id);

    Optional<ServiceOrder> findByAppointmentId(UUID appointmentId);

    List<ServiceOrder> findByBikeId(UUID bikeId);

    long nextOrderSequence();
}
