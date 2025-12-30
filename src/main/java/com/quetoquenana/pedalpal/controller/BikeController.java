package com.quetoquenana.pedalpal.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.CreateBikeRequest;
import com.quetoquenana.pedalpal.dto.CreateComponentRequest;
import com.quetoquenana.pedalpal.dto.UpdateBikeRequest;
import com.quetoquenana.pedalpal.model.Bike;
import com.quetoquenana.pedalpal.model.BikeComponent;
import com.quetoquenana.pedalpal.service.BikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/bikes")
@RequiredArgsConstructor
@Slf4j
public class BikeController {

    private final BikeService bikeService;

    @PostMapping
    @JsonView(Bike.BikeDetail.class)
    public ResponseEntity<Bike> createBike(@Valid @RequestBody CreateBikeRequest request) {
        log.info("Received request to create bike: {}", request);
        Bike saved = bikeService.createBike(request);
        return ResponseEntity.created(URI.create("/api/bikes/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    @JsonView(Bike.BikeDetail.class)
    public ResponseEntity<Bike> updateBike(@PathVariable UUID id, @Valid @RequestBody UpdateBikeRequest request) {
        log.info("Received request to update bike id={} payload={}", id, request);
        Bike saved = bikeService.updateBike(id, request);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBike(@PathVariable UUID id) {
        log.info("Received request to soft-delete bike id={}", id);
        bikeService.softDeleteBike(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/components")
    @JsonView(BikeComponent.BikeComponentDetail.class)
    public ResponseEntity<BikeComponent> addComponent(@PathVariable UUID id, @Valid @RequestBody CreateComponentRequest request) {
        log.info("Received request to add component to bike id={} payload={}", id, request);
        BikeComponent saved = bikeService.addComponent(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
