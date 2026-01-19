package com.quetoquenana.pedalpal.service;

import com.quetoquenana.pedalpal.dto.api.response.SystemCodeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SystemCodeDtoService {

    Page<SystemCodeResponse> findAll(Pageable pageable);

    List<SystemCodeResponse> findByCategory(String category);

    Optional<SystemCodeResponse> findById(UUID id);
}
