package com.quetoquenana.pedalpal.store.presentation.controller;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.common.presentation.dto.ApiResponse;
import com.quetoquenana.pedalpal.security.application.CurrentUserProvider;
import com.quetoquenana.pedalpal.store.application.query.StoreQueryService;
import com.quetoquenana.pedalpal.store.application.result.StoreResult;
import com.quetoquenana.pedalpal.store.mapper.StoreApiMapper;
import com.quetoquenana.pedalpal.store.presentation.dto.response.StoreResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreController {

    private final StoreQueryService queryService;

    private final StoreApiMapper apiMapper;

    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/{id}")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> getById(
            @PathVariable("id") UUID id,
            @RequestParam(name = "locationStatus", required = false) Set<GeneralStatus> locationStatuses
    ) {
        log.info("GET /v1/api/stores/{} Received request to get store by id", id);
        StoreResult result = queryService.getById(id);
        StoreResponse response = apiMapper.toResponse(result, locationStatuses);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> findAll() {
        log.info("GET /v1/api/store Received request to find all stores");
        List<StoreResult> result = queryService.getAll();
        List<StoreResponse> response = result.stream().map(apiMapper::toResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }
}
