package com.quetoquenana.pedalpal.controller;

import com.quetoquenana.pedalpal.dto.api.request.CreateCfImageRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateCfImageRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.model.local.CfImage;
import com.quetoquenana.pedalpal.service.CfImageService;
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
import java.util.Optional;
import java.util.UUID;

import static com.quetoquenana.pedalpal.util.Constants.Pagination.PAGE;
import static com.quetoquenana.pedalpal.util.Constants.Pagination.PAGE_SIZE;

@RestController
@RequestMapping("/v1/api/images")
@RequiredArgsConstructor
@Slf4j
public class CfImageController {

    private final CfImageService cfImageService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> fetchAll(
            @RequestParam(defaultValue = PAGE) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size
    ) {
        log.info("GET /v1/api/images called");
        Page<CfImage> entities = cfImageService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(new JsonViewPageUtil<>(entities, entities.getPageable())));
    }

    @GetMapping("/provider/{provider}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> fetchByProvider(@PathVariable String provider) {
        log.info("GET /v1/api/images/provider/{} called", provider);
        List<CfImage> list = cfImageService.findByProvider(provider);
        return ResponseEntity.ok(new ApiResponse(list));
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<ApiResponse> fetchByOwnerId(@PathVariable UUID ownerId) {
        log.info("GET /v1/api/images/owner/{} called", ownerId);
        List<CfImage> list = cfImageService.findByOwnerId(ownerId);
        return ResponseEntity.ok(new ApiResponse(list));
    }

    @GetMapping("/provider-asset/{providerAssetId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<ApiResponse> getByProviderAssetId(@PathVariable String providerAssetId) {
        log.info("GET /v1/api/images/provider-asset/{} called", providerAssetId);
        Optional<CfImage> opt = cfImageService.findByProviderAssetId(providerAssetId);
        return ResponseEntity.ok(new ApiResponse(opt.orElse(null)));
    }

    @GetMapping("/context/{contextCode}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<ApiResponse> getByContextCode(@PathVariable UUID contextCode) {
        log.info("GET /v1/api/images/context/{} called", contextCode);
        List<CfImage> list = cfImageService.findByContextCode(contextCode);
        return ResponseEntity.ok(new ApiResponse(list));
    }

    @GetMapping("/reference/{referenceId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<ApiResponse> getByReferenceId(@PathVariable UUID referenceId) {
        log.info("GET /v1/api/images/reference/{} called", referenceId);
        List<CfImage> list = cfImageService.findByReferenceId(referenceId);
        return ResponseEntity.ok(new ApiResponse(list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<ApiResponse> getById(@PathVariable UUID id) {
        log.info("GET /v1/api/images/{} called", id);
        CfImage entity = cfImageService.findById(id);
        return ResponseEntity.ok(new ApiResponse(entity));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateCfImageRequest request) {
        log.info("POST /v1/api/images Received request to create image: {}", request);
        CfImage saved = cfImageService.createImage(request);
        return ResponseEntity.created(URI.create("/v1/api/images/" + saved.getId()))
                .body(new ApiResponse(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable UUID id, @Valid @RequestBody UpdateCfImageRequest request) {
        log.info("PUT /v1/api/images/{} Received request to update image: {}", id, request);
        CfImage saved = cfImageService.updateImage(id, request);
        return ResponseEntity.ok(new ApiResponse(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID id) {
        log.info("DELETE /v1/api/images/{} called", id);
        cfImageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}

