package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.api.request.CreateSystemCodeRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateSystemCodeRequest;
import com.quetoquenana.pedalpal.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.data.SystemCode;
import com.quetoquenana.pedalpal.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.service.SystemCodeService;
import com.quetoquenana.pedalpal.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SystemCodeServiceImpl implements SystemCodeService {

    private final SystemCodeRepository repository;

    @Override
    public Optional<SystemCode> findByCategoryAndCode(String category, String code) {
        return repository.findByCategoryAndCode(category, code);
    }

    @Override
    public Optional<SystemCode> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public SystemCode create(CreateSystemCodeRequest request) {
        SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );
        SystemCode code = SystemCode.createFromRequest(request);
        return repository.save(code);
    }

    @Override
    @Transactional
    public SystemCode update(UUID id, UpdateSystemCodeRequest request) {
        SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );
        SystemCode existing = repository.findById(id).orElseThrow(() -> new RecordNotFoundException("systemcode.not.found", id));
        existing.updateFromRequest(request);
        return repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        SystemCode existing = repository.findById(id).orElseThrow(() -> new RecordNotFoundException("systemcode.not.found", id));
        repository.delete(existing);
    }
}
