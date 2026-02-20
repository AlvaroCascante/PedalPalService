package com.quetoquenana.pedalpal.bike.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.bike.domain.model.SystemCode;
import com.quetoquenana.pedalpal.bike.domain.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.mapper.SystemCodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SystemCodeRepositoryImpl implements SystemCodeRepository {

    private final SystemCodeJpaRepository repository;
    private final SystemCodeMapper mapper;

    @Override
    public Optional<SystemCode> getById(UUID systemCodeId) {
        return repository.findById(systemCodeId).map(mapper::toDomain);
    }

    @Override
    public SystemCode save(SystemCode systemCode) {
        return null;
    }

    @Override
    public SystemCode update(UUID systemCodeId, SystemCode systemCode) {
        return null;
    }

    @Override
    public void deleteById(UUID systemCodeId) {

    }

    @Override
    public Optional<SystemCode> findByCategoryAndCode(String category, String code) {
        return repository.findByCategoryAndCode(category, code).map(mapper::toDomain);
    }
}
