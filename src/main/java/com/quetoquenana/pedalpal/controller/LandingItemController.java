package com.quetoquenana.pedalpal.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.api.request.CreateLandingItemRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateLandingItemRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.model.local.LandingPageItem;
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
@RequestMapping("/v1/api/landing-items")
@RequiredArgsConstructor
@Slf4j
public class LandingItemController {

    private final LandingItemService landingItemService;

    @GetMapping("")
    @JsonView(LandingPageItem.LandingList.class)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> fetchAll(
            @RequestParam(defaultValue = PAGE) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size
    ) {
        log.info("GET /v1/api/landing-items/all called");
        Page<LandingPageItem> entities = landingItemService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(new JsonViewPageUtil<>(entities, entities.getPageable())));
    }

    @GetMapping("/status/{statusId}")
    @JsonView(LandingPageItem.LandingList.class)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> findByStatus(
            @PathVariable UUID statusId,
            @RequestParam(defaultValue = PAGE) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size

    ) {
        log.info("GET /v1/api/landing-items/status/{} called", statusId);
        Page<LandingPageItem> entities = landingItemService.findByStatus(statusId, PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(new JsonViewPageUtil<>(entities, entities.getPageable())));
    }

    @GetMapping("/{id}")
    @JsonView(LandingPageItem.LandingDetail.class)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getById(@PathVariable UUID id) {
        log.info("GET /v1/api/landing-items/{} called", id);
        LandingPageItem item = landingItemService.findById(id);
        return ResponseEntity.ok(new ApiResponse(item));
    }

    @PostMapping
    @JsonView(LandingPageItem.LandingDetail.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateLandingItemRequest request) {
        log.info("POST /v1/api/landing-items called");
        LandingPageItem saved = landingItemService.create(request);
        return ResponseEntity.created(URI.create("/v1/api/landing-items/" + saved.getId())).body(new ApiResponse(saved));
    }

    @PutMapping("/{id}")
    @JsonView(LandingPageItem.LandingDetail.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateLandingItemRequest request) {
        log.info("PUT /v1/api/landing-items/{} called", id);
        LandingPageItem saved = landingItemService.update(id, request);
        return ResponseEntity.ok(new ApiResponse(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("DELETE /v1/api/landing-items/{} called", id);
        landingItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
