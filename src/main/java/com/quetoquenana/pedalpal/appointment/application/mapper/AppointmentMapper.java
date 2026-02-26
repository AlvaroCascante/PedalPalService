package com.quetoquenana.pedalpal.appointment.application.mapper;

import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentListItemResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentServiceResult;
import com.quetoquenana.pedalpal.appointment.domain.model.Appointment;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppointmentMapper {

    public AppointmentResult toResult(Appointment appointment) {
        List<AppointmentServiceResult> requestedServices = appointment.getRequestedServices().stream()
                .map(this::toRequestedServiceResult)
                .toList();

        return AppointmentResult.builder()
                .id(appointment.getId())
                .bikeId(appointment.getBikeId())
                .storeLocationId(appointment.getStoreLocationId())
                .scheduledAt(appointment.getScheduledAt())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .requestedServices(requestedServices)
                .build();
    }

    public AppointmentListItemResult toListItemResult(Appointment appointment) {
        return AppointmentListItemResult.builder()
                .id(appointment.getId())
                .bikeId(appointment.getBikeId())
                .storeLocationId(appointment.getStoreLocationId())
                .scheduledAt(appointment.getScheduledAt())
                .status(appointment.getStatus())
                .build();
    }

    private AppointmentServiceResult toRequestedServiceResult(AppointmentService rs) {
        return AppointmentServiceResult.builder()
                .id(rs.getId())
                .productId(rs.getProduct().getId())
                .productNameSnapshot(rs.getProductNameSnapshot())
                .priceSnapshot(rs.getPriceSnapshot())
                .build();
    }

    public Appointment toAppointment(CreateAppointmentCommand command) {
        return Appointment.builder()
                .bikeId(command.bikeId())
                .storeLocationId(command.storeLocationId())
                .scheduledAt(command.scheduledAt())
                .notes(command.notes())
                .build();
    }
}

