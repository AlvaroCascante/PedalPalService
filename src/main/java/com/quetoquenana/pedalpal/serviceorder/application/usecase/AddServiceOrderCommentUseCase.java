package com.quetoquenana.pedalpal.serviceorder.application.usecase;

import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceorder.application.command.AddServiceOrderCommentCommand;
import com.quetoquenana.pedalpal.serviceorder.application.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceorder.application.result.ServiceOrderCommentResult;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderComment;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderCommentRepository;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

/**
 * Use case for adding comments to a service order.
 */
@RequiredArgsConstructor
public class AddServiceOrderCommentUseCase {

    private static final int MAX_COMMENT_LENGTH = 1000;

    private final AuthenticatedUserPort authenticatedUserPort;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderCommentRepository commentRepository;
    private final ServiceOrderMapper mapper;
    private final Clock clock;

    /**
     * Adds a comment to a service order.
     */
    @Transactional
    public ServiceOrderCommentResult execute(AddServiceOrderCommentCommand command) {
        AuthenticatedUser authenticatedUser = authenticatedUserPort.getAuthenticatedUser()
                .orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        serviceOrderRepository.getById(command.serviceOrderId())
                .orElseThrow(() -> new RecordNotFoundException("service.order.not.found", command.serviceOrderId()));

        validateComment(command.comment());
        Instant now = Instant.now(clock);
        ServiceOrderComment comment = mapper.toModel(command, authenticatedUser, now);

        ServiceOrderComment saved = commentRepository.save(comment);

        return mapper.toResult(saved);
    }


    private void validateComment(String comment) {
        if (comment == null || comment.isBlank()) {
            throw new BadRequestException("service.order.comment.blank");
        }
        if (comment.length() > MAX_COMMENT_LENGTH) {
            throw new BadRequestException("service.order.comment.max.length");
        }
    }
}
