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

        return AppointmentResult.builder()
                .id(model.getId())
                .bikeId(model.getBikeId())
                .storeLocationId(model.getStoreLocationId())
                .scheduledAt(model.getScheduledAt())
                .status(model.getStatus())
                .notes(model.getNotes())
                .requestedServices(requestedServices)
                .build();
    }

    private AppointmentServiceResult toResult(RequestedService model) {
        return AppointmentServiceResult.builder()
                .id(model.getId())
                .productId(model.getProductId())
                .productNameSnapshot(model.getName())
                .priceSnapshot(model.getPrice())
                .build();
    }

    public Appointment toModel(CreateAppointmentCommand command) {
        return Appointment.builder()
                .bikeId(command.bikeId())
                .storeLocationId(command.storeLocationId())
                .scheduledAt(command.scheduledAt())
                .notes(command.notes())
                .build();
    }

    public AppointmentListItemResult toListItemResult(Appointment model) {
        return AppointmentListItemResult.builder()
                .id(model.getId())
                .bikeId(model.getBikeId())
                .storeLocationId(model.getStoreLocationId())
                .scheduledAt(model.getScheduledAt())
                .status(model.getStatus())
                .build();
    }
}

