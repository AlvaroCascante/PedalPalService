package com.quetoquenana.pedalpal.serviceOrder.repository;

import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrder;

import java.util.Optional;
import java.util.UUID;

public interface ServiceOrderRepository {
    ServiceOrder save(ServiceOrder model);

    Optional<ServiceOrder> getById(UUID id);

    Optional<ServiceOrder> getByAppointmentId(UUID appointmentId);
}

