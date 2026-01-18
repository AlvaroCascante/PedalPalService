package com.quetoquenana.pedalpal.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.quetoquenana.pedalpal.dto.api.request.CreateComponentRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateComponentRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.model.data.BikeComponent;
import com.quetoquenana.pedalpal.service.BikeComponentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/components")
@RequiredArgsConstructor
@Slf4j
public class BikeComponentController {

    private final BikeComponentService componentService;

    @PutMapping("/replace/{componentId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @JsonView(BikeComponent.BikeComponentDetail.class)
    public ResponseEntity<ApiResponse> replaceComponent(@PathVariable UUID componentId,
                                                        @Valid @RequestBody CreateComponentRequest request) {
        log.info("PUT /v1/api/components/replace/{} called", componentId);
        BikeComponent saved = componentService.replaceComponent(componentId, request);
        return ResponseEntity.ok(new ApiResponse(saved));
    }

    @PutMapping("/{componentId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @JsonView(BikeComponent.BikeComponentDetail.class)
    public ResponseEntity<ApiResponse> updateComponent(@PathVariable UUID componentId,
                                                       @Valid @RequestBody UpdateComponentRequest request) {
        log.info("PUT /v1/api/components/{} called", componentId);
        BikeComponent saved = componentService.updateComponent(componentId, request);
        return ResponseEntity.ok(new ApiResponse(saved));
    }

    @DeleteMapping("/{componentId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> removeComponent(@PathVariable UUID componentId) {
        log.info("DELETE /v1/api/components/{} called", componentId);
        componentService.removeComponent(componentId);
        return ResponseEntity.noContent().build();
    }
}

