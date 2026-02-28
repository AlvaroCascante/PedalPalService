package com.quetoquenana.pedalpal.systemCode.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.systemCode.mapper.SystemCodeEntityMapper;
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
        return repository.findById(systemCodeId).map(SystemCodeEntityMapper::toModel);
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
        return repository.findByCategoryAndCode(category, code).map(SystemCodeEntityMapper::toModel);
    }

    @Override
    public List<SystemCode> findByCategoryAndStatus(String category, GeneralStatus status) {
        return repository.findByCategoryAndStatus(category, status)
                .stream()
                .map(SystemCodeEntityMapper::toModel)
                .toList();
    }
}
