package com.quetoquenana.pedalpal.infrastructure.persistence.bike.repository;

import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.infrastructure.persistence.bike.entity.BikeEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.mapper.BikeEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BikeRepositoryImpl implements BikeRepository {

    private final BikeJpaRepository repository;
    private final BikeEntityMapper mapper;

    @Override
    public Bike save(Bike bike) {
        // Map the Bike domain model to a BikeEntity
        BikeEntity entity = mapper.toEntity(bike);
        return mapper.toModel(repository.save(entity));
    }

    @Override
    public Optional<Bike> getById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsBySerialNumber(String serialNumber) {
        return repository.existsBySerialNumber(serialNumber);
    }

    @Override
    public Optional<Bike> findByIdAndOwnerId(UUID id, UUID ownerId) {
        return repository.findByIdAndOwnerId(id, ownerId).map(mapper::toModel);
    }

    @Override
    public List<Bike> findByOwnerIdAndStatus(UUID ownerId, BikeStatus bikeStatus) {
        return repository.findByOwnerIdAndStatus(ownerId, bikeStatus)
                .stream()
                .map(mapper::toModel)
                .toList();
    }
}

