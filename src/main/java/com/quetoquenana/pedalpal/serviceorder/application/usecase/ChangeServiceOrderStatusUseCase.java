package com.quetoquenana.pedalpal.serviceorder.application.usecase;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceorder.application.command.ChangeServiceOrderStatusCommand;
import com.quetoquenana.pedalpal.serviceorder.application.handler.ServiceOrderTransitionHandler;
import com.quetoquenana.pedalpal.serviceorder.application.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceorder.application.result.ChangeServiceOrderStatusResult;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Single entry point for service order status transitions.
 */
@RequiredArgsConstructor
public class ChangeServiceOrderStatusUseCase {

    private final ServiceOrderMapper mapper;
    private final ServiceOrderRepository repository;
    private final AuthenticatedUserPort authenticatedUserPort;
    private final List<ServiceOrderTransitionHandler> transitionHandlers;

    /**
     * Changes a service order status and executes transition side effects.
     */
    @Transactional
    public ChangeServiceOrderStatusResult execute(ChangeServiceOrderStatusCommand command) {
        ServiceOrder serviceOrder = repository.getById(command.serviceOrderId())
                .orElseThrow(() -> new RecordNotFoundException("service.order.not.found", command.serviceOrderId()));

        AuthenticatedUser authenticatedUser = authenticatedUserPort.getAuthenticatedUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        ServiceOrderStatus fromStatus = serviceOrder.getStatus();
        ServiceOrderStatus toStatus = ServiceOrderStatus.from(command.toStatus());

        serviceOrder.changeStatus(toStatus);

        transitionHandlers.stream()
                .filter(handler -> handler.supports(fromStatus, toStatus))
                .forEach(handler -> handler.handle(serviceOrder, command, authenticatedUser));

        ServiceOrder saved = repository.save(serviceOrder);
        return mapper.toResult(saved, fromStatus);
    }
}
