package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.api.response.SystemCodeResponse;
import com.quetoquenana.pedalpal.model.data.SystemCode;
import com.quetoquenana.pedalpal.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.service.SystemCodeDtoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemCodeDtoServiceImpl implements SystemCodeDtoService {

    private final SystemCodeRepository repository;
    private final MessageSource messageSource;

    @Override
    public Page<SystemCodeResponse> findAll(Pageable pageable) {
        Page<SystemCode> page = repository.findAll(pageable);
        List<SystemCodeResponse> list = page.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    @Override
    public List<SystemCodeResponse> findByCategory(String category) {
        return repository.findByCategory(category).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public java.util.Optional<SystemCodeResponse> findById(UUID id) {
        return repository.findById(id).map(this::toResponse);
    }

    private SystemCodeResponse toResponse(SystemCode systemCode) {
        String resolved = null;
        String key = systemCode.getCodeKey();
        if (key != null && !key.isBlank()) {
            Locale locale = LocaleContextHolder.getLocale();
            try {
                resolved = messageSource.getMessage(key, null, locale);
            } catch (Exception ex) {
                log.warn("Message key '{}' not found for locale '{}'", key, locale);
            }
        }
        return SystemCodeResponse.fromEntity(systemCode, resolved);
    }
}
