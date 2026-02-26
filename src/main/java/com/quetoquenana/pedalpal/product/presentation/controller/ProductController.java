package com.quetoquenana.pedalpal.product.presentation.controller;

import com.quetoquenana.pedalpal.common.dto.ApiResponse;
import com.quetoquenana.pedalpal.product.application.query.ProductQueryService;
import com.quetoquenana.pedalpal.product.application.result.ProductPackageResult;
import com.quetoquenana.pedalpal.product.application.result.ProductResult;
import com.quetoquenana.pedalpal.product.mapper.ProductApiMapper;
import com.quetoquenana.pedalpal.product.presentation.dto.response.ProductPackageResponse;
import com.quetoquenana.pedalpal.product.presentation.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductQueryService queryService;

    private final ProductApiMapper apiMapper;

    @GetMapping("/products/{id}")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> getProductById(
            @PathVariable("id") UUID id
    ) {
        log.info("GET /v1/api/products/{} Received request to get product by id", id);
        ProductResult result = queryService.getProductById(id);
        ProductResponse response = apiMapper.toProductResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/packages/{id}")
    @PreAuthorize("(hasRole('USER')) or (hasRole('ADMIN'))")
    public ResponseEntity<ApiResponse> getProductPackageById(
            @PathVariable("id") UUID id
    ) {
        log.info("GET /v1/api/products/{} Received request to get product package by id", id);
        ProductPackageResult result = queryService.getProductPackageById(id);
        ProductPackageResponse response = apiMapper.toProductPackageResponse(result);
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/products/active")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> findActiveProducts() {
        log.info("GET /v1/api/packages/active Received request to find active products");
        List<ProductResult> result = queryService.getActiveProducts();
        List<ProductResponse> response = result.stream().map(apiMapper::toProductResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }

    @GetMapping("/packages/active")
    @PreAuthorize("(hasRole('USER'))")
    public ResponseEntity<ApiResponse> findActivePackages() {
        log.info("GET /v1/api/packages/active Received request to find active packages");
        List<ProductPackageResult> result = queryService.getActiveProductPackages();
        List<ProductPackageResponse> response = result.stream().map(apiMapper::toProductPackageResponse).toList();
        return ResponseEntity.ok(new ApiResponse(response));
    }
}
