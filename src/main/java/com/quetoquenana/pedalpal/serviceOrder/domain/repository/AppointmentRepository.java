package com.quetoquenana.pedalpal.serviceOrder.domain.repository;

import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {
    ServiceOrder save(ServiceOrder appointment);

    Optional<ServiceOrder> getById(UUID id);

    List<ServiceOrder> findUpcomingByBikeId(UUID bikeId, Instant now);

    List<ServiceOrder> findPastByBikeId(UUID bikeId, Instant now);
}

