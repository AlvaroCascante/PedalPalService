package com.quetoquenana.pedalpal.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.BikeEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.mapper.BikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BikeRepositoryImpl implements BikeRepository {

    private final BikeJpaRepository repository;
    private final BikeMapper mapper;

    @Override
    public Bike save(Bike bike) {

        // Map the Bike domain model to a BikeEntity
        BikeEntity entity = mapper.toEntity(bike);

        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Bike update(UUID bikeId, Bike bike) {
        return null;
    }

    @Override
    public Optional<Bike> getById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<Bike> findByOwnerId(UUID ownerId) {
        return List.of();
    }

    @Override
    public List<Bike> findByOwnerIdAndStatusCodes(UUID ownerId, List<String> codes) {
        return List.of();
    }

    @Override
    public boolean existsBySerialNumber(String serialNumber) {
        return repository.existsBySerialNumber(serialNumber);
    }
}

