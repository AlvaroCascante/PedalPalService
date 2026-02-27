package com.quetoquenana.pedalpal.appointment.mapper;

import com.quetoquenana.pedalpal.appointment.application.command.CreateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentCommand;
import com.quetoquenana.pedalpal.appointment.application.command.UpdateAppointmentStatusCommand;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentListItemResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentServiceResult;
import com.quetoquenana.pedalpal.appointment.application.result.AppointmentResult;
import com.quetoquenana.pedalpal.appointment.application.result.ConfirmAppointmentResult;
import com.quetoquenana.pedalpal.appointment.domain.model.AppointmentStatus;
import com.quetoquenana.pedalpal.appointment.presentation.dto.request.*;
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

    public CreateAppointmentCommand toCreateCommand(CreateAppointmentRequest request, UUID authenticatedUserId) {
        List<CreateAppointmentCommand.RequestedServiceCommand> items = request.requestedServices() == null
                ? null
                : request.requestedServices().stream()
                .map(i -> CreateAppointmentCommand.RequestedServiceCommand.builder().productId(i.productId()).build())
                .toList();

        return CreateAppointmentCommand.builder()
                .bikeId(request.bikeId())
                .storeLocationId(request.storeLocationId())
                .scheduledAt(request.scheduledAt())
                .notes(request.notes())
                .requestedServices(items)
                .build();
    }

    public UpdateAppointmentCommand toUpdateCommand(UUID id, UpdateAppointmentRequest request, UUID authenticatedUserId) {
        List<CreateAppointmentCommand.RequestedServiceCommand> items = request.requestedServices() == null
                ? null
                : request.requestedServices().stream()
                .map(i -> CreateAppointmentCommand.RequestedServiceCommand.builder().productId(i.productId()).build())
                .toList();

        return UpdateAppointmentCommand.builder()
                .id(id)
                .storeLocationId(request.storeLocationId())
                .scheduledAt(request.scheduledAt())
                .notes(request.notes())
                .requestedServices(items)
                .build();
    }

    public UpdateAppointmentStatusCommand toStatusCommand(UUID id, UpdateAppointmentStatusRequest request, UUID authenticatedUserId) {
        return UpdateAppointmentStatusCommand.builder()
                .id(id)
                .status(request.status())
                .build();
    }


    public UpdateAppointmentStatusCommand toStatusConfirmCommand(UUID id, UUID authenticatedUserId) {
        return UpdateAppointmentStatusCommand.builder()
                .id(id)
                .status(AppointmentStatus.CONFIRMED.name())
                .build();
    }

    public AppointmentResponse toResponse(AppointmentResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        List<AppointmentServiceResponse> requestedServices = result.requestedServices() == null
                ? List.of()
                : result.requestedServices().stream().map(this::toRequestedServiceResponse).toList();

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
                : result.appointmentResult().requestedServices().stream().map(this::toRequestedServiceResponse).toList();

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

    private AppointmentServiceResponse toRequestedServiceResponse(AppointmentServiceResult rs) {
        return new AppointmentServiceResponse(
                rs.id(),
                rs.productId(),
                rs.productNameSnapshot(),
                rs.priceSnapshot()
        );
    }
}

