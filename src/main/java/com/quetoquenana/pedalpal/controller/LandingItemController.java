package com.quetoquenana.pedalpal.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.CreateLandingItemRequest;
import com.quetoquenana.pedalpal.dto.UpdateLandingItemRequest;
import com.quetoquenana.pedalpal.model.ApiResponse;
import com.quetoquenana.pedalpal.model.LandingItem;
import com.quetoquenana.pedalpal.service.LandingItemService;
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
import java.util.UUID;

import static com.quetoquenana.pedalpal.util.Constants.Pagination.PAGE;
import static com.quetoquenana.pedalpal.util.Constants.Pagination.PAGE_SIZE;

@RestController
@RequestMapping("/api/landing-items")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class LandingItemController {

    private final LandingItemService service;

    @PostMapping
    @JsonView(LandingItem.LandingDetail.class)
    public ResponseEntity<LandingItem> create(@Valid @RequestBody CreateLandingItemRequest req) {
        log.info("POST /api/landing-items called payload={}", req);
        LandingItem saved = service.create(req);
        return ResponseEntity.created(URI.create("/api/landing-items/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    @JsonView(LandingItem.LandingDetail.class)
    public ResponseEntity<LandingItem> getById(@PathVariable UUID id) {
        log.info("GET /api/landing-items/{} called", id);
        LandingItem item = service.findById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/page")
    @JsonView(LandingItem.LandingList.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getPage(
            @RequestParam(defaultValue = PAGE) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size
    ) {
        log.info("GET /api/landing-items/page called with page={}, size={}", page, size);
        Page<LandingItem> entities = service.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(new JsonViewPageUtil<>(entities, entities.getPageable())));
    }

    @GetMapping("/page/status")
    @JsonView(LandingItem.LandingList.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = PAGE) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size
    ) {
        log.info("GET /api/landing-items/page/status called with status={}, page={}, size={}", status, page, size);
        Page<LandingItem> entities = service.findByStatus(status, PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(new JsonViewPageUtil<>(entities, entities.getPageable())));
    }

    @GetMapping("/page/category")
    @JsonView(LandingItem.LandingList.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = PAGE) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size
    ) {
        log.info("GET /api/landing-items/page/category called with category={}, page={}, size={}", category, page, size);
        Page<LandingItem> entities = service.findByCategory(category, PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(new JsonViewPageUtil<>(entities, entities.getPageable())));
    }

    @PutMapping("/{id}")
    @JsonView(LandingItem.LandingDetail.class)
    public ResponseEntity<LandingItem> update(@PathVariable UUID id, @Valid @RequestBody UpdateLandingItemRequest req) {
        log.info("PUT /api/landing-items/{} called payload={}", id, req);
        LandingItem updated = service.update(id, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("DELETE /api/landing-items/{} called", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
