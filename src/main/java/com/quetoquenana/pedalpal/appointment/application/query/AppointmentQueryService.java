package com.quetoquenana.pedalpal.appointment.application.query;

import com.quetoquenana.pedalpal.appointment.application.result.AppointmentListItemResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.appointment.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AppointmentQueryService {

    private final AppointmentMapper mapper;
    private final AppointmentRepository repository;

    public AppointmentResult getById(UUID id) {
        Appointment model = repository.getById(id)
                .orElseThrow(() -> new RecordNotFoundException("appointment.not.found"));
        return mapper.toResult(model);
    }

    public Set<AppointmentListItemResult> getUpcomingAppointments(UUID bikeId) {
        List<Appointment> models = repository.findUpcomingByBikeId(bikeId, Instant.now());
        return models.stream().map(mapper::toListItemResult).collect(Collectors.toSet());
    }

    public Set<AppointmentListItemResult> getPastAppointments(UUID bikeId) {
        List<Appointment> models = repository.findPastByBikeId(bikeId, Instant.now());
        return models.stream().map(mapper::toListItemResult).collect(Collectors.toSet());
    }
}
