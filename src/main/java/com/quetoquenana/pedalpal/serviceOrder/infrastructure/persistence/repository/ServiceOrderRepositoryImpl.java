package com.quetoquenana.pedalpal.serviceOrder.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.serviceOrder.mapper.ServiceOrderEntityMapper;
import com.quetoquenana.pedalpal.serviceOrder.infrastructure.persistence.entity.ServiceOrderEntity;
import com.quetoquenana.pedalpal.serviceOrder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceOrder.domain.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ServiceOrderRepositoryImpl implements ServiceOrderRepository {

    private final ServiceOrderJpaRepository repository;
    private final ServiceOrderEntityMapper mapper;


    @Override
    public ServiceOrder save(ServiceOrder model) {
        ServiceOrderEntity entity = mapper.toEntity(model);
        return mapper.toModel(repository.save(entity));
    }

    @Override
    public Optional<ServiceOrder> getById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<ServiceOrder> getByAppointmentId(UUID appointmentId) {
        return repository.getByAppointmentId(appointmentId).map(mapper::toModel);
    }
}
