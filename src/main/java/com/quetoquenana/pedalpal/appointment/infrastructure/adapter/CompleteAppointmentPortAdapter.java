package com.quetoquenana.pedalpal.appointment.infrastructure.adapter;

import com.quetoquenana.pedalpal.appointment.application.service.CompleteAppointmentFromServiceOrderService;
import com.quetoquenana.pedalpal.serviceorder.application.port.CompleteAppointmentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adapter that bridges service-order completion with appointment completion logic.
 */
@Component
@RequiredArgsConstructor
public class CompleteAppointmentPortAdapter implements CompleteAppointmentPort {

    private final CompleteAppointmentFromServiceOrderService completeAppointmentService;

    @Override
    public void completeAppointment(UUID appointmentId) {
        completeAppointmentService.complete(appointmentId);
    }
}
