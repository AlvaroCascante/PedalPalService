package com.quetoquenana.pedalpal.infrastructure.persistence.systemCode.repository;

import com.quetoquenana.pedalpal.infrastructure.persistence.mapper.SystemCodeEntityMapper;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import com.quetoquenana.pedalpal.systemCode.domain.repository.SystemCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SystemCodeRepositoryImpl implements SystemCodeRepository {

    private final SystemCodeJpaRepository repository;

    @Override
    public Optional<SystemCode> getById(UUID systemCodeId) {
        return repository.findById(systemCodeId).map(SystemCodeEntityMapper::toSystemCode);
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
    public Optional<SystemCode> findByCategoryAndCode(String category, String code) {
        return repository.findByCategoryAndCode(category, code).map(SystemCodeEntityMapper::toSystemCode);
    }

    @Override
    public List<SystemCode> findByCategoryAndStatus(String category, String status) {
        return repository.findByCategoryAndStatus(category, status)
                .stream()
                .map(SystemCodeEntityMapper::toSystemCode)
                .toList();
    }
}
