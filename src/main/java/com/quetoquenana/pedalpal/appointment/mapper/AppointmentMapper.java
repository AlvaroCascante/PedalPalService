package com.quetoquenana.pedalpal.appointment.mapper;

import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentListItemResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentServiceResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.RequestedService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppointmentMapper {

    public AppointmentResult toResult(Appointment model) {
        List<AppointmentServiceResult> requestedServices = model.getRequestedServices().stream()
                .map(this::toResult)
                .toList();

        return new AppointmentResult(
                model.getId(),
                model.getBikeId(),
                model.getStoreLocationId(),
                model.getScheduledAt(),
                model.getStatus(),
                model.getNotes(),
                requestedServices
        );
    }

    private AppointmentServiceResult toResult(RequestedService model) {
        return new AppointmentServiceResult(
                model.getId(),
                model.getServiceId(),
                model.getName(),
                model.getPrice()
        );
    }

    public Appointment toModel(CreateAppointmentCommand command) {
        // The requested services are handled separately in the use case,
        // so we only map the basic appointment fields here
        return Appointment.builder()
                .bikeId(command.bikeId())
                .storeLocationId(command.storeLocationId())
                .scheduledAt(command.scheduledAt())
                .notes(command.notes())
                .build();
    }

    public AppointmentListItemResult toListItemResult(Appointment model) {
        return new AppointmentListItemResult(
                model.getId(),
                model.getBikeId(),
                model.getStoreLocationId(),
                model.getScheduledAt(),
                model.getStatus()
        );
    }
}
