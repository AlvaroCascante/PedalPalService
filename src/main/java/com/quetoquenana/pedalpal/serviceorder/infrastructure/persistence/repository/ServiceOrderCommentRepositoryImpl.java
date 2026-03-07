package com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrderComment;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderCommentRepository;
import com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.entity.ServiceOrderCommentEntity;
import com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.mapper.ServiceOrderCommentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository adapter for service order comments.
 */
@Repository
@RequiredArgsConstructor
public class ServiceOrderCommentRepositoryImpl implements ServiceOrderCommentRepository {

    private final ServiceOrderCommentJpaRepository repository;

    @Override
    public ServiceOrderComment save(ServiceOrderComment comment) {
        ServiceOrderCommentEntity entity = ServiceOrderCommentEntityMapper.toEntity(comment);
        return ServiceOrderCommentEntityMapper.toModel(repository.save(entity));
    }

    @Override
    public List<ServiceOrderComment> findByServiceOrderId(UUID serviceOrderId) {
        return repository.findByServiceOrderIdOrderByCreatedAtAsc(serviceOrderId)
                .stream()
                .map(ServiceOrderCommentEntityMapper::toModel)
                .toList();
    }
}

