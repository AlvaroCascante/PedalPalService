package com.quetoquenana.pedalpal.serviceorder.application.mapper;

import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import com.quetoquenana.pedalpal.serviceorder.application.command.AddServiceOrderCommentCommand;
import com.quetoquenana.pedalpal.serviceorder.application.result.*;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderComment;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderDetail;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.quetoquenana.pedalpal.common.domain.model.UserType.*;

public class ServiceOrderMapper {

    public ServiceOrderResult toResult(ServiceOrder model) {
        List<ServiceOrderDetailResult> requestedServices = model.getRequestedServices().stream()
                .map(this::toResult)
                .toList();

        return new ServiceOrderResult(
                model.getId(),
                model.getAppointmentId(),
                model.getBikeId(),
                model.getStartedAt(),
                model.getCompletedAt(),
                model.getStatus(),
                model.getOrderNumber(),
                model.getTotalPrice(),
                model.getNotes(),
                requestedServices
        );
    }

    private ServiceOrderDetailResult toResult(ServiceOrderDetail model) {
        return new ServiceOrderDetailResult(
                model.getId(),
                model.getProductId(),
                model.getTechnicianId(),
                model.getProductNameSnapshot(),
                model.getPriceSnapshot(),
                model.getStatus(),
                model.getStartedAt(),
                model.getCompletedAt(),
                model.getNotes()
        );
    }

    public ChangeServiceOrderStatusResult toResult(ServiceOrder model, ServiceOrderStatus fromStatus) {
        return new ChangeServiceOrderStatusResult(
                model.getId(),
                model.getOrderNumber(),
                fromStatus,
                model.getStatus(),
                model.getStartedAt(),
                model.getCompletedAt()
        );
    }

    public ServiceOrderCommentResult toResult (ServiceOrderComment serviceOrderComment) {
        return new ServiceOrderCommentResult(
                serviceOrderComment.getId(),
                serviceOrderComment.getServiceOrderId(),
                serviceOrderComment.getComment(),
                serviceOrderComment.getCustomerVisible(),
                serviceOrderComment.getCreatedByType(),
                serviceOrderComment.getCreatedAt(),
                serviceOrderComment.getCreatedBy()
        );
    }

    public ServiceOrderComment toModel(
            AddServiceOrderCommentCommand command,
            AuthenticatedUser authenticatedUser,
            Instant now
    ) {
        return ServiceOrderComment.builder()
                .id(UUID.randomUUID())
                .serviceOrderId(command.serviceOrderId())
                .comment(command.comment())
                .customerVisible(command.customerVisible())
                .createdByType(getUserType(authenticatedUser.type()))
                .createdAt(now)
                .createdBy(authenticatedUser.userId())
                .build();
    }

    private UserType getUserType(UserType type) {
        return switch (type) {
            case CUSTOMER -> CUSTOMER;
            case TECHNICIAN -> TECHNICIAN;
            case ADMIN -> ADMIN;
            case SYSTEM, UNKNOWN -> SYSTEM;
        };
    }
}
