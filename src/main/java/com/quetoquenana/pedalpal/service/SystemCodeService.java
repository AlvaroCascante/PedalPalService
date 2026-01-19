package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.api.request.CreateSystemCodeRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateSystemCodeRequest;
import com.quetoquenana.pedalpal.model.local.SystemCode;

import java.util.Optional;
import java.util.UUID;

public interface SystemCodeService {

    Optional<SystemCode> findByCategoryAndCode(String category, String code);

    Optional<SystemCode> findById(UUID id);

    SystemCode create(CreateSystemCodeRequest request);

    SystemCode update(UUID id, UpdateSystemCodeRequest request);

    void delete(UUID id);
}
