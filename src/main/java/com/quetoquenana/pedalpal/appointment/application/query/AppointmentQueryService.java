package com.quetoquenana.pedalpal.appointment.application.query;

import com.quetoquenana.pedalpal.appointment.application.result.AppointmentListItemResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.appointment.application.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AppointmentQueryService {

    private final AppointmentMapper mapper;
    private final AppointmentRepository repository;
    private final AuthenticatedUserPort authenticatedUserPort;

    public AppointmentResult getById(UUID id) {
        Appointment model = repository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("appointment.not.found"));
        return mapper.toResult(model);
    }

    public List<AppointmentListItemResult> getAppointments() {
        AuthenticatedUser authenticatedUser = authenticatedUserPort.getAuthenticatedUser()
                .orElseThrow(() -> new RecordNotFoundException("authentication.required"));

        List<Appointment> models = repository.findByCustomerId(authenticatedUser.userId());
        return models.stream().map(mapper::toListItemResult).toList();
    }

    public List<AppointmentListItemResult> getUpcomingAppointments(UUID bikeId) {
        List<Appointment> models = repository.findUpcomingByBikeId(bikeId, Instant.now());
        return models.stream().map(mapper::toListItemResult).toList();
    }

    public List<AppointmentListItemResult> getPastAppointments(UUID bikeId) {
        List<Appointment> models = repository.findPastByBikeId(bikeId, Instant.now());
        return models.stream().map(mapper::toListItemResult).toList();
    }
}
