package com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderComment;
import com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.entity.ServiceOrderCommentEntity;

/**
 * Maps service order comment persistence entities to domain models and back.
 */
public class ServiceOrderCommentEntityMapper {

    private ServiceOrderCommentEntityMapper() {
    }

    /**
     * Maps a persistence entity to a domain model.
     */
    public static ServiceOrderComment toModel(ServiceOrderCommentEntity entity) {
        return ServiceOrderComment.builder()
                .id(entity.getId())
                .serviceOrderId(entity.getServiceOrderId())
                .comment(entity.getComment())
                .customerVisible(entity.getCustomerVisible())
                .createdByType(entity.getCreatedByType())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    /**
     * Maps a domain model to a persistence entity.
     */
    public static ServiceOrderCommentEntity toEntity(ServiceOrderComment model) {
        return ServiceOrderCommentEntity.builder()
                .id(model.getId())
                .serviceOrderId(model.getServiceOrderId())
                .comment(model.getComment())
                .customerVisible(model.getCustomerVisible())
                .createdByType(model.getCreatedByType())
                .createdAt(model.getCreatedAt())
                .createdBy(model.getCreatedBy())
                .build();
    }
}

