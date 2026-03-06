package com.quetoquenana.pedalpal.appointment.presentation.mapper;

import com.quetoquenana.pedalpal.appointment.application.command.ChangeAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.command.RequestedServiceCommand;
import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentListItemResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentServiceResult;
import com.quetoquenana.pedalpal.appointment.application.result.ChangeAppointmentStatusResult;
import com.quetoquenana.pedalpal.appointment.domain.model.ServiceType;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.ChangeAppointmentStatusRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.CreateAppointmentRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.UpdateAppointmentRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.AppointmentListItemResponse;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.AppointmentResponse;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.AppointmentServiceResponse;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.ChangeAppointmentStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AppointmentApiMapper {

    private final MessageSource messageSource;

    public CreateAppointmentCommand toCommand(CreateAppointmentRequest request) {
        List<RequestedServiceCommand> items = request.requestedServices() == null
                ? null
                : request.requestedServices().stream()
                .map(i -> new RequestedServiceCommand(
                        i.serviceId(),
                        ServiceType.from(i.serviceType())))
                .toList();

        return new CreateAppointmentCommand(
                request.bikeId(),
                request.storeLocationId(),
                request.scheduledAt(),
                request.notes(),
                items
        );
    }

    public UpdateAppointmentCommand toCommand(UUID id, UpdateAppointmentRequest request) {
        return new UpdateAppointmentCommand(
                id,
                request.scheduledAt(),
                request.notes()
        );
    }

    public ChangeAppointmentStatusCommand toCommand(UUID id, ChangeAppointmentStatusRequest request) {
        return new ChangeAppointmentStatusCommand(
                id,
                request.toStatus(),
                request.closureReason(),
                request.technicianId(),
                request.note()
        );
    }

    public AppointmentResponse toResponse(AppointmentResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        Set<AppointmentServiceResponse> requestedServices = result.requestedServices() == null
                ? Set.of()
                : result.requestedServices().stream().map(this::toResponse).collect(Collectors.toSet());

        return new AppointmentResponse(
                result.id(),
                result.bikeId(),
                result.storeLocationId(),
                result.scheduledAt(),
                statusLabel,
                result.notes(),
                requestedServices
        );
    }

    public ChangeAppointmentStatusResponse toResponse(ChangeAppointmentStatusResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String fromStatus = messageSource.getMessage(result.fromStatus().getKey(), null, locale);
        String toStatus = messageSource.getMessage(result.toStatus().getKey(), null, locale);

        return new ChangeAppointmentStatusResponse(
                result.appointmentId(),
                fromStatus,
                toStatus,
                result.changedAt(),
                result.serviceOrderNumber()
        );
    }

    private AppointmentServiceResponse toResponse(AppointmentServiceResult result) {
        return new AppointmentServiceResponse(
                result.id(),
                result.productId(),
                result.productNameSnapshot(),
                result.priceSnapshot()
        );
    }

    public AppointmentListItemResponse toListItemResponse(AppointmentListItemResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        return new AppointmentListItemResponse(
                result.id(),
                result.bikeId(),
                result.storeLocationId(),
                result.scheduledAt(),
                statusLabel
        );
    }
}
