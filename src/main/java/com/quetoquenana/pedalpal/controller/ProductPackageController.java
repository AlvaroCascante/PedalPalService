package com.quetoquenana.pedalpal.controller;

import com.quetoquenana.pedalpal.dto.api.request.CreateProductPackageRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateProductPackageRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiResponse;
import com.quetoquenana.pedalpal.model.local.ProductPackage;
import com.quetoquenana.pedalpal.service.ProductPackageService;
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
@RequestMapping("/v1/api/packages")
@RequiredArgsConstructor
@Slf4j
public class ProductPackageController {

    private final ProductPackageService packageService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<ApiResponse> fetchAll(
            @RequestParam(defaultValue = PAGE) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size
    ) {
        log.info("GET /v1/api/packages called");
        Page<ProductPackage> entities = packageService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse(new JsonViewPageUtil<>(entities, entities.getPageable())));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<ApiResponse> fetchByStatus(@PathVariable String status) {
        log.info("GET /v1/api/packages/status/{} called", status);
        List<ProductPackage> list = packageService.findByStatus(status);
        return ResponseEntity.ok(new ApiResponse(list));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER'))")
    public ResponseEntity<ApiResponse> getById(@PathVariable UUID id) {
        log.info("GET /v1/api/packages/{} called", id);
        ProductPackage entity = packageService.findById(id);
        return ResponseEntity.ok(new ApiResponse(entity));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateProductPackageRequest request) {
        log.info("POST /v1/api/packages Received request to create package: {}", request);
        ProductPackage saved = packageService.createPackage(request);
        return ResponseEntity.created(URI.create("/v1/api/packages/" + saved.getId()))
                .body(new ApiResponse(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updatePackage(@PathVariable UUID id, @Valid @RequestBody UpdateProductPackageRequest request) {
        log.info("PUT /v1/api/packages/{} Received request to update package: {}", id, request);
        ProductPackage saved = packageService.updatePackage(id, request);
        return ResponseEntity.ok(new ApiResponse(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePackage(@PathVariable UUID id) {
        log.info("DELETE /v1/api/packages/{} called", id);
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{packageId}/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addProduct(@PathVariable UUID packageId, @PathVariable UUID productId) {
        log.info("POST /v1/api/packages/{}/products/{} called", packageId, productId);
        ProductPackage updated = packageService.addProduct(packageId, productId);
        return ResponseEntity.ok(new ApiResponse(updated));
    }

    @DeleteMapping("/{packageId}/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> removeProduct(@PathVariable UUID packageId, @PathVariable UUID productId) {
        log.info("DELETE /v1/api/packages/{}/products/{} called", packageId, productId);
        ProductPackage updated = packageService.removeProduct(packageId, productId);
        return ResponseEntity.ok(new ApiResponse(updated));
    }
}

