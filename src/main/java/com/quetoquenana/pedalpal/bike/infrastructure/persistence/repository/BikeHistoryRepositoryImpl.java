package com.quetoquenana.pedalpal.bike.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.bike.domain.model.BikeHistory;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeHistoryRepository;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.BikeHistoryEntity;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.mapper.BikeEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BikeHistoryRepositoryImpl implements BikeHistoryRepository {

    private final BikeHistoryJpaRepository repository;

    @Override
    public Optional<BikeHistory> getById(UUID id) {
        return repository.findById(id).map(BikeEntityMapper::toModel);
    }

    @Override
    public void save(BikeHistory bikeHistory) {
        BikeHistoryEntity entity = BikeEntityMapper.toEntity(bikeHistory);
        BikeEntityMapper.toModel(repository.save(entity));
    }

    @Override
    public List<BikeHistory> findByBikeId(UUID bikeId) {
        return repository.findByBikeId(bikeId)
                .stream()
                .map(BikeEntityMapper::toModel)
                .toList();
    }
}

