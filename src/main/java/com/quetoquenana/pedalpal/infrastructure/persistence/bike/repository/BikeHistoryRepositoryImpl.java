package com.quetoquenana.pedalpal.infrastructure.persistence.bike.repository;

import com.quetoquenana.pedalpal.bike.domain.model.BikeHistory;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeHistoryRepository;
import com.quetoquenana.pedalpal.infrastructure.persistence.bike.entity.BikeHistoryEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.bike.mapper.BikeEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BikeHistoryRepositoryImpl implements BikeHistoryRepository {

    private final BikeHistoryJpaRepository repository;
    private final BikeEntityMapper mapper;

    @Override
    public Optional<BikeHistory> getById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public BikeHistory save(BikeHistory bikeHistory) {
        BikeHistoryEntity entity = mapper.toEntity(bikeHistory);
        return mapper.toModel(repository.save(entity));
    }

    @Override
    public List<BikeHistory> findByBikeId(UUID bikeId) {
        return repository.findByBikeId(bikeId)
                .stream()
                .map(mapper::toModel)
                .toList();
    }
}

