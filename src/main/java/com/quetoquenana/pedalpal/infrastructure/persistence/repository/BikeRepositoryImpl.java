package com.quetoquenana.pedalpal.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.BikeComponentEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.BikeEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.mapper.BikeEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BikeRepositoryImpl implements BikeRepository {

    private final BikeJpaRepository repository;
    private final BikeEntityMapper mapper;

    @Override
    public Bike save(Bike bike) {
        // Map the Bike domain model to a BikeEntity
        BikeEntity entity = mapper.toBikeEntity(bike);

        Set<BikeComponentEntity> components = bike.getComponents()
                .stream()
                .map(component -> mapper.toBikeComponentEntity(entity, component))
                .collect(Collectors.toSet());

        entity.setComponents(components);
        return mapper.toBike(repository.save(entity));
    }

    @Override
    public Optional<Bike> getById(UUID id) {
        return repository.findById(id).map(mapper::toBike);
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
        return repository.findByIdAndOwnerId(id, ownerId).map(mapper::toBike);
    }

    @Override
    public List<Bike> findByOwnerIdAndStatus(UUID ownerId, String bikeStatus) {
        return repository.findByOwnerIdAndStatus(ownerId, bikeStatus)
                .stream()
                .map(mapper::toBike)
                .toList();
    }
}

