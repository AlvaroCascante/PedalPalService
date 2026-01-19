package com.quetoquenana.pedalpal.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.api.request.CreateStoreLocationRequest;
import com.quetoquenana.pedalpal.dto.api.request.CreateStoreRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateStoreLocationRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateStoreRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.model.data.Store;
import com.quetoquenana.pedalpal.model.data.StoreLocation;
import com.quetoquenana.pedalpal.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreController {

    private final StoreService storeService;

    @GetMapping()
    @JsonView(Store.StoreList.class)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> fetchAllStores() {
        log.info("GET /v1/api/stores/all called");
        List<Store> entities = storeService.findAll();
        return ResponseEntity.ok(new ApiResponse(entities));
    }

    @GetMapping("/{id}")
    @JsonView(Store.StoreDetail.class)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getById(@PathVariable UUID id) {
        log.info("GET /v1/api/stores/{} called", id);
        Store entity = storeService.findById(id);
        return ResponseEntity.ok(new ApiResponse(entity));
    }

    @PostMapping
    @JsonView(Store.StoreDetail.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateStoreRequest request) {
        log.info("POST /v1/api/stores Received request to create store: {}", request);
        Store saved = storeService.createStore(request);
        return ResponseEntity.created(URI.create("/v1/api/stores/" + saved.getId()))
                .body(new ApiResponse(saved));
    }

    @PutMapping("/{id}")
    @JsonView(Store.StoreDetail.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateStore(@PathVariable UUID id, @Valid @RequestBody UpdateStoreRequest request) {
        log.info("PUT /v1/api/stores/{} Received request to update store: {}", id, request);
        Store saved = storeService.updateStore(id, request);
        return ResponseEntity.ok(new ApiResponse(saved));
    }

    @PostMapping("/{storeId}/locations")
    @JsonView(StoreLocation.StoreLocationDetail.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addStoreLocation(@PathVariable UUID storeId,
                                                        @Valid @RequestBody CreateStoreLocationRequest request) {
        log.info("POST /v1/api/stores/{}/locations called", storeId);
        StoreLocation saved = storeService.addStoreLocation(storeId, request);
        return ResponseEntity.created(URI.create("/v1/api/stores/locations/" + saved.getId()))
                .body(new ApiResponse(saved));
    }

    @DeleteMapping("/locations/{locationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeStoreLocation(@PathVariable UUID locationId) {
        log.info("DELETE /v1/api/stores/locations/{} called", locationId);
        storeService.removeStoreLocation(locationId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/locations/{locationId}")
    @JsonView(StoreLocation.StoreLocationDetail.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateStoreLocation(@PathVariable UUID locationId,
                                                           @Valid @RequestBody UpdateStoreLocationRequest request) {
        log.info("PATCH /v1/api/stores/locations/{} called", locationId);
        StoreLocation saved = storeService.updateStoreLocation(locationId, request);
        return ResponseEntity.ok(new ApiResponse(saved));
    }

}
