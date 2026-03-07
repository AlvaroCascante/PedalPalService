package com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.entity.ServiceOrderCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data repository for service order comment entities.
 */
public interface ServiceOrderCommentJpaRepository extends JpaRepository<ServiceOrderCommentEntity, UUID> {

    /**
     * Finds comments by service order id ordered by createdAt ascending.
     */
    List<ServiceOrderCommentEntity> findByServiceOrderIdOrderByCreatedAtAsc(UUID serviceOrderId);
}

