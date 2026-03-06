package com.quetoquenana.pedalpal.appointment.application.handler;

import com.quetoquenana.pedalpal.appointment.application.command.ChangeAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;

import java.util.UUID;

/**
 * Handles side effects for specific appointment transitions.
 */
public interface AppointmentTransitionHandler {

    /**
     * @return true when this handler applies for a specific transition.
     */
    boolean supports(AppointmentStatus fromStatus, AppointmentStatus toStatus);

    /**
     * Executes transition side effects and optionally returns a service order number.
     */
    String handle(
            Appointment appointment,
            AppointmentStatus fromStatus,
            ChangeAppointmentStatusCommand command,
            UUID actorUserId
    );
}
