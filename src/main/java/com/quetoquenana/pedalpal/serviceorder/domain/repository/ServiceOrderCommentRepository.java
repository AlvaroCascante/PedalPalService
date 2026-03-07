package com.quetoquenana.pedalpal.serviceorder.domain.repository;

import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderComment;

import java.util.List;
import java.util.UUID;

/**
 * Domain repository for service order comments.
 */
public interface ServiceOrderCommentRepository {

    /**
     * Persists a new comment.
     */
    ServiceOrderComment save(ServiceOrderComment comment);

    /**
     * Retrieves comments for a service order ordered by creation time ascending.
     */
    List<ServiceOrderComment> findByServiceOrderId(UUID serviceOrderId);
}
