package com.quetoquenana.pedalpal.systemCode.presentation.controller;

import com.quetoquenana.pedalpal.common.dto.ApiResponse;
import com.quetoquenana.pedalpal.systemCode.application.query.SystemCodeQueryService;
import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;
import com.quetoquenana.pedalpal.systemCode.mapper.SystemCodeApiMapper;
import com.quetoquenana.pedalpal.systemCode.presentation.dto.response.SystemCodeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
@Slf4j
public class SystemCodeController {

    private final SystemCodeQueryService queryService;

    private final SystemCodeApiMapper apiMapper;

    @GetMapping("/system-codes/{id}")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> getById(
            @PathVariable("id") UUID id
    ) {
        log.info("GET /v1/api/components/{} Received request to get component by id", id);
        SystemCodeResult result = queryService.getById(id);
        SystemCodeResponse response = apiMapper.toResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/components")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> getActiveComponents() {
        log.info("GET /v1/api/components Received request to get active components");
        List<SystemCodeResult> result = queryService.getActiveComponents();
        List<SystemCodeResponse> response = result.stream().map(apiMapper::toResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }
}
