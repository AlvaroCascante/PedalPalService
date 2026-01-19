package com.quetoquenana.pedalpal.controller;

import com.quetoquenana.pedalpal.dto.api.request.CreateSystemCodeRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateSystemCodeRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.dto.api.response.SystemCodeResponse;
import com.quetoquenana.pedalpal.model.data.SystemCode;
import com.quetoquenana.pedalpal.service.SystemCodeDtoService;
import com.quetoquenana.pedalpal.service.SystemCodeService;
import com.quetoquenana.pedalpal.util.JsonViewPageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.quetoquenana.pedalpal.util.Constants.Pagination.PAGE;
import static com.quetoquenana.pedalpal.util.Constants.Pagination.PAGE_SIZE;

@RestController
@RequestMapping("/v1/api/system-codes")
@RequiredArgsConstructor
@Slf4j
public class SystemCodeController {

    private final SystemCodeService systemCodeService;
    private final SystemCodeDtoService systemCodeDtoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> fetchAll(
            @RequestParam(defaultValue = PAGE) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size
    ) {
        log.info("GET /v1/api/system-codes called");
        Page<SystemCodeResponse> entities = systemCodeDtoService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(new JsonViewPageUtil<>(entities, entities.getPageable())));
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getByCategory(@PathVariable String category) {
        log.info("GET /v1/api/system-codes/category/{} called", category);
        List<SystemCodeResponse> list = systemCodeDtoService.findByCategory(category);
        return ResponseEntity.ok(new ApiResponse(list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getById(@PathVariable UUID id) {
        log.info("GET /v1/api/system-codes/{} called", id);
        SystemCodeResponse entity = systemCodeDtoService.findById(id).orElseThrow(() -> new RuntimeException("systemcode.not.found"));
        return ResponseEntity.ok(new ApiResponse(entity));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateSystemCodeRequest request) {
        log.info("POST /v1/api/system-codes Received request to create systemCode: {}", request);
        SystemCode saved = systemCodeService.create(request);
        return ResponseEntity.created(URI.create("/v1/api/system-codes/" + saved.getId()))
                .body(new ApiResponse(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateSystemCodeRequest request) {
        log.info("PUT /v1/api/system-codes/{} Received request to update systemCode: {}", id, request);
        SystemCode saved = systemCodeService.update(id, request);
        return ResponseEntity.ok(new ApiResponse(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("DELETE /v1/api/system-codes/{} called", id);
        systemCodeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
