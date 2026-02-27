package com.quetoquenana.pedalpal.appointment.mapper;

import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.command.RequestedServiceCommand;
import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentListItemResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentServiceResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.result.ConfirmAppointmentResult;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.domain.model.ServiceType;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.CreateAppointmentRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.UpdateAppointmentRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.UpdateAppointmentStatusRequest;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.AppointmentListItemResponse;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.AppointmentServiceResponse;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.AppointmentResponse;
import com.quetoquenana.pedalpal.appointment.presentation.dto.response.ConfirmAppointmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AppointmentApiMapper {

    private final MessageSource messageSource;

    public CreateAppointmentCommand toCommand(CreateAppointmentRequest request, UUID authenticatedUserId) {
        List<RequestedServiceCommand> items = request.requestedServices() == null
                ? null
                : request.requestedServices().stream()
                .map(i -> new RequestedServiceCommand(
                        i.serviceId(),
                        ServiceType.from(i.serviceType())))
                .toList();

        return new CreateAppointmentCommand(
                request.bikeId(),
                authenticatedUserId,
                request.storeLocationId(),
                request.scheduledAt(),
                request.notes(),
                items
        );
    }

    public UpdateAppointmentCommand toCommand(UUID id, UpdateAppointmentRequest request) {
        List<RequestedServiceCommand> items = request.requestedServices() == null
                ? null
                : request.requestedServices().stream()
                .map(i -> new RequestedServiceCommand(
                        i.serviceId(),
                        ServiceType.from(i.serviceType())))
                .toList();

        return new UpdateAppointmentCommand(
                id,
                request.storeLocationId(),
                request.scheduledAt(),
                request.notes(),
                items
        );
    }

    public UpdateAppointmentStatusCommand toCommand(UUID id, UpdateAppointmentStatusRequest request) {
        return new UpdateAppointmentStatusCommand(id, request.status());
    }

    public UpdateAppointmentStatusCommand toCommand(UUID id) {
        return new UpdateAppointmentStatusCommand(id, AppointmentStatus.CONFIRMED.name());
    }

    public AppointmentResponse toResponse(AppointmentResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        List<AppointmentServiceResponse> requestedServices = result.requestedServices() == null
                ? List.of()
                : result.requestedServices().stream().map(this::toResponse).toList();

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

    public ConfirmAppointmentResponse toResponse(ConfirmAppointmentResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String statusLabel = messageSource.getMessage(result.appointmentResult().status().getKey(), null, locale);

        List<AppointmentServiceResponse> requestedServices = result.appointmentResult().requestedServices() == null
                ? List.of()
                : result.appointmentResult().requestedServices().stream().map(this::toResponse).toList();

        return new ConfirmAppointmentResponse(
                result.appointmentResult().id(),
                result.appointmentResult().bikeId(),
                result.appointmentResult().storeLocationId(),
                result.appointmentResult().scheduledAt(),
                statusLabel,
                result.appointmentResult().notes(),
                result.serviceOrderResult().id().toString(),
                requestedServices
        );
    }

    private AppointmentServiceResponse toResponse(AppointmentServiceResult rs) {
        return new AppointmentServiceResponse(
                rs.id(),
                rs.productId(),
                rs.productNameSnapshot(),
                rs.priceSnapshot()
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
