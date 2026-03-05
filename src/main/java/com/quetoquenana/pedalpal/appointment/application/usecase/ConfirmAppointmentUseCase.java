package com.quetoquenana.pedalpal.appointment.application.usecase;

import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.result.ConfirmAppointmentResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.repository.AppointmentRepository;
import com.quetoquenana.pedalpal.appointment.application.mapper.AppointmentMapper;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceOrder.application.port.ServiceOrderPort;
import com.quetoquenana.pedalpal.serviceOrder.application.result.ServiceOrderResult;
import com.quetoquenana.pedalpal.store.domain.model.StoreLocation;
import com.quetoquenana.pedalpal.store.domain.repository.StoreLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * Use case for confirming an appointment.
 * It retrieves the appointment, updates its status to CONFIRMED
 * Creates a service order based on the appointment details.
 * It also handles exceptions and logs errors.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmAppointmentUseCase {

    private final AppointmentMapper mapper;
    private final AppointmentRepository repository;
    private final StoreLocationRepository storeLocationRepository;
    private final ServiceOrderPort serviceOrderPort;

    @Transactional
    public ConfirmAppointmentResult execute(UpdateAppointmentStatusCommand command) {
        Appointment appointment = repository.getById(command.id())
                .orElseThrow(() -> new RecordNotFoundException("appointment.not.found"));

        StoreLocation location = storeLocationRepository.getById(appointment.getStoreLocationId())
                .orElseThrow(() -> new RecordNotFoundException("store.location.not.found"));

        appointment.confirm();

        repository.save(appointment);

        ServiceOrderResult serviceOrderResult = serviceOrderPort.creteServiceOrder(appointment, location.getStorePrefix());

        return new ConfirmAppointmentResult(
                mapper.toResult(appointment),
                serviceOrderResult
        );
    }
}

