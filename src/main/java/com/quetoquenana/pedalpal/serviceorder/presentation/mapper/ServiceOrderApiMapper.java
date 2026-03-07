package com.quetoquenana.pedalpal.serviceorder.presentation.mapper;

import com.quetoquenana.pedalpal.serviceorder.application.command.AddServiceOrderCommentCommand;
import com.quetoquenana.pedalpal.serviceorder.application.command.ChangeServiceOrderStatusCommand;
import com.quetoquenana.pedalpal.serviceorder.application.result.ChangeServiceOrderStatusResult;
import com.quetoquenana.pedalpal.serviceorder.application.result.ServiceOrderCommentResult;
import com.quetoquenana.pedalpal.serviceorder.application.result.ServiceOrderDetailResult;
import com.quetoquenana.pedalpal.serviceorder.application.result.ServiceOrderResult;
import com.quetoquenana.pedalpal.serviceorder.presentation.dto.request.AddServiceOrderCommentRequest;
import com.quetoquenana.pedalpal.serviceorder.presentation.dto.request.ChangeServiceOrderStatusRequest;
import com.quetoquenana.pedalpal.serviceorder.presentation.dto.response.ChangeServiceOrderStatusResponse;
import com.quetoquenana.pedalpal.serviceorder.presentation.dto.response.ServiceOrderCommentResponse;
import com.quetoquenana.pedalpal.serviceorder.presentation.dto.response.ServiceOrderDetailResponse;
import com.quetoquenana.pedalpal.serviceorder.presentation.dto.response.ServiceOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RequiredArgsConstructor
public class ServiceOrderApiMapper {

    private final MessageSource messageSource;

    /**
     * Maps API request data to status-change command.
     */
    public ChangeServiceOrderStatusCommand toCommand(UUID id, ChangeServiceOrderStatusRequest request) {
        return new ChangeServiceOrderStatusCommand(
                id,
                request.toStatus(),
                request.technicianId(),
                request.note()
        );
    }

    /**
     * Maps status-change result to API response DTO.
     */
    public ChangeServiceOrderStatusResponse toResponse(ChangeServiceOrderStatusResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String fromStatus = messageSource.getMessage(result.fromStatus().getKey(), null, locale);
        String toStatus = messageSource.getMessage(result.toStatus().getKey(), null, locale);

        return new ChangeServiceOrderStatusResponse(
                result.serviceOrderId(),
                result.orderNumber(),
                fromStatus,
                toStatus,
                result.startedAt(),
                result.completedAt()
        );
    }

    /**
     * Maps API request data to add-comment command.
     */
    public AddServiceOrderCommentCommand toCommand(UUID id, AddServiceOrderCommentRequest request) {
        return new AddServiceOrderCommentCommand(
                id,
                request.comment(),
                request.customerVisible()
        );
    }

    /**
     * Maps add-comment result to API response DTO.
     */
    public ServiceOrderCommentResponse toResponse(ServiceOrderCommentResult result) {
        return new ServiceOrderCommentResponse(
                result.id(),
                result.serviceOrderId(),
                result.comment(),
                result.customerVisible(),
                result.createdByType().name(),
                result.createdAt(),
                result.createdBy()
        );
    }

    public ServiceOrderResponse toResponse(ServiceOrderResult result) {
        List<ServiceOrderDetailResponse> requestedServices = result.requestedServices().stream()
                .map(this::toResult)
                .toList();

        return new ServiceOrderResponse(
                result.id(),
                result.appointmentId(),
                result.bikeId(),
                result.startedAt(),
                result.completedAt(),
                result.status(),
                result.orderNumber(),
                result.totalPrice(),
                result.notes(),
                requestedServices
        );
    }

    private ServiceOrderDetailResponse toResult(ServiceOrderDetailResult result) {
        return new ServiceOrderDetailResponse(
                result.id(),
                result.productId(),
                result.technicianId(),
                result.productName(),
                result.price(),
                result.status(),
                result.startedAt(),
                result.completedAt(),
                result.notes()
        );
    }
}
