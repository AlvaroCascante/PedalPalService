package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.model.data.SystemCode;
import com.quetoquenana.pedalpal.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.service.SystemCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SystemCodeServiceImpl implements SystemCodeService {

    private final SystemCodeRepository repository;

    @Override
    public Optional<SystemCode> findByCategoryAndCode(String category, String code) {
        return repository.findByCategoryAndCode(category, code);
    }
}

