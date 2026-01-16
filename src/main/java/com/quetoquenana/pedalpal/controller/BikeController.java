package com.quetoquenana.pedalpal.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.api.request.CreateBikeRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateBikeRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.model.data.Bike;
import com.quetoquenana.pedalpal.service.BikeService;
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
@RequestMapping("/api/bikes")
@RequiredArgsConstructor
@Slf4j
public class BikeController {

    private final BikeService bikeService;

    @GetMapping("/all")
    @JsonView(Bike.BikeList.class)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> fetchAllBikes() {
        log.info("GET /api/bikes/all called");

        List<Bike> entities = bikeService.findAll();
        return ResponseEntity.ok(new ApiResponse(entities));
    }


    @GetMapping("/active")
    @JsonView(Bike.BikeList.class)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> fetchActiveBikes() {
        log.info("GET /api/bikes/active");

        List<Bike> entities = bikeService.findActive();
        return ResponseEntity.ok(new ApiResponse(entities));
    }

    @GetMapping("/{id}")
    @JsonView(Bike.BikeDetail.class)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<ApiResponse> getById(@PathVariable UUID id) {
        log.info("GET /api/bikes/{} called", id);

        Bike entity = bikeService.findById(id);
        return ResponseEntity.ok(new ApiResponse(entity));
    }

    @PostMapping
    @JsonView(Bike.BikeDetail.class)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and @securityService.validateOwnerId(#request.ownerId))")
    public ResponseEntity<Bike> create(@Valid @RequestBody CreateBikeRequest request) {
        log.info("Received request to create bike: {}", request);

        Bike saved = bikeService.createBike(request);
        return ResponseEntity.created(URI.create("/api/bikes/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    @JsonView(Bike.BikeDetail.class)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<Bike> updateBike(@PathVariable UUID id, @Valid @RequestBody UpdateBikeRequest request) {
        log.info("Received request to update bike id={} payload={}", id, request);
        Bike saved = bikeService.updateBike(id, request);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<Void> deleteBike(@PathVariable UUID id) {
        log.info("Received request to soft-delete bike id={}", id);
        bikeService.softDeleteBike(id);
        return ResponseEntity.noContent().build();
    }
}
