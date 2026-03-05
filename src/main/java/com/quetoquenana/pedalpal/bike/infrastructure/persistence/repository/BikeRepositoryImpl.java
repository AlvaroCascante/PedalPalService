package com.quetoquenana.pedalpal.bike.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.bike.domain.model.BikeStatus;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.BikeEntity;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.mapper.BikeEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BikeRepositoryImpl implements BikeRepository {

    private final BikeJpaRepository repository;

    @Override
    public Bike save(Bike bike) {
        // Map the Bike domain model to a BikeEntity
        BikeEntity entity = BikeEntityMapper.toEntity(bike);
        return BikeEntityMapper.toModel(repository.save(entity));
    }

    @Override
    public Optional<Bike> getById(UUID id) {
        return repository.findById(id).map(BikeEntityMapper::toModel);
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
    public boolean existsBydAndOwnerId(UUID id, UUID ownerId) {
        return repository.existsByIdAndOwnerId(id, ownerId);
    }

    @Override
    public Optional<Bike> findByIdAndOwnerId(UUID id, UUID ownerId) {
        return repository.findByIdAndOwnerId(id, ownerId).map(BikeEntityMapper::toModel);
    }

    @Override
    public List<Bike> findByOwnerIdAndStatus(UUID ownerId, BikeStatus bikeStatus) {
        return repository.findByOwnerIdAndStatus(ownerId, bikeStatus)
                .stream()
                .map(BikeEntityMapper::toModel)
                .toList();
    }
}

