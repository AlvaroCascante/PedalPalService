package com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.serviceorder.domain.model.ServiceOrder;
import com.quetoquenana.pedalpal.serviceorder.domain.repository.ServiceOrderRepository;
import com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.entity.ServiceOrderEntity;
import com.quetoquenana.pedalpal.serviceorder.infrastructure.persistence.mapper.ServiceOrderEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ServiceOrderRepositoryImpl implements ServiceOrderRepository {

    private final ServiceOrderJpaRepository repository;


    @Override
    public ServiceOrder save(ServiceOrder model) {
        ServiceOrderEntity entity = ServiceOrderEntityMapper.toEntity(model);
        return ServiceOrderEntityMapper.toModel(repository.save(entity));
    }

    @Override
    public Optional<ServiceOrder> getById(UUID id) {
        return repository.findById(id).map(ServiceOrderEntityMapper::toModel);
    }

    @Override
    public Optional<ServiceOrder> findByAppointmentId(UUID appointmentId) {
        return repository.findByAppointmentId(appointmentId).map(ServiceOrderEntityMapper::toModel);
    }

    @Override
    public List<ServiceOrder> findByBikeId(UUID bikeId) {
        return repository.findByBikeId(bikeId).stream()
                .map(ServiceOrderEntityMapper::toModel)
                .toList();
    }

    @Override
    public long nextOrderSequence() {
        return repository.nextOrderSequence();
    }
}
