package com.quetoquenana.pedalpal.serviceorder.application.handler;

import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.serviceorder.application.command.ChangeServiceOrderStatusCommand;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderStatus;

/**
 * Handles side effects for specific service order transitions.
 */
public interface ServiceOrderTransitionHandler {

    /**
     * @return true when this handler applies for a specific transition.
     */
    boolean supports(ServiceOrderStatus fromStatus, ServiceOrderStatus toStatus);

    /**
     * Executes transition side effects.
     */
    void handle(ServiceOrder serviceOrder, ChangeServiceOrderStatusCommand command, AuthenticatedUser user);
}
