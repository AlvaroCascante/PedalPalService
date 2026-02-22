package com.quetoquenana.pedalpal.infrastructure.persistence.bike.repository;

import com.quetoquenana.pedalpal.infrastructure.persistence.mapper.SystemCodeMapper;
import com.quetoquenana.pedalpal.common.domain.model.SystemCode;
import com.quetoquenana.pedalpal.common.domain.repository.SystemCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SystemCodeRepositoryImpl implements SystemCodeRepository {

    private final SystemCodeJpaRepository repository;

    @Override
    public Optional<SystemCode> getById(UUID systemCodeId) {
        return repository.findById(systemCodeId).map(SystemCodeMapper::toSystemCode);
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
        return repository.findByCategoryAndCode(category, code).map(SystemCodeMapper::toSystemCode);
    }
}
