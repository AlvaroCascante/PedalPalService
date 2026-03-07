package com.quetoquenana.pedalpal.serviceorder.application.query;

import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.serviceorder.application.mapper.ServiceOrderMapper;
import com.quetoquenana.pedalpal.serviceorder.application.result.ServiceOrderCommentResult;
import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderComment;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderCommentRepository;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Query service responsible for retrieving comments for a service order.
 */
@RequiredArgsConstructor
public class GetServiceOrderCommentsQueryService {

    private final ServiceOrderMapper mapper;
    private final ServiceOrderCommentRepository commentRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    /**
     * Returns comments for a service order ordered by creation time ascending.
     */
    public List<ServiceOrderCommentResult> getByServiceOrderId(UUID serviceOrderId) {
        serviceOrderRepository.getById(serviceOrderId)
                .orElseThrow(() -> new RecordNotFoundException("service.order.not.found", serviceOrderId));

        List<ServiceOrderComment> response = commentRepository.findByServiceOrderId(serviceOrderId);
        return response.stream()
                .map(mapper::toResult)
                .toList();
    }
}