package com.quetoquenana.pedalpal.domain.repository;

import com.quetoquenana.pedalpal.domain.model.SystemCode;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SystemCodeRepository {

    Optional<SystemCode> getById(UUID systemCodeId);

    SystemCode save(SystemCode systemCode);

    SystemCode update(UUID systemCodeId, SystemCode systemCode);

    void deleteById(UUID systemCodeId);

    Optional<SystemCode> findByCategoryAndCode(String category, String code);
}

