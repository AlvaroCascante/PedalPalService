package com.quetoquenana.pedalpal.systemCode.domain.repository;

import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SystemCodeRepository {

    Optional<SystemCode> getById(UUID systemCodeId);

    SystemCode save(SystemCode systemCode);

    SystemCode update(UUID systemCodeId, SystemCode systemCode);

    Optional<SystemCode> findByCategoryAndCode(String category, String code);

    List<SystemCode> findByCategoryAndStatus(String category, String status);
}