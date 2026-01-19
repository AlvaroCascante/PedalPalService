package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.api.request.CreateProductPackageRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateProductPackageRequest;
import com.quetoquenana.pedalpal.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.local.Product;
import com.quetoquenana.pedalpal.model.local.ProductPackage;
import com.quetoquenana.pedalpal.model.local.SystemCode;
import com.quetoquenana.pedalpal.repository.ProductPackageRepository;
import com.quetoquenana.pedalpal.repository.ProductRepository;
import com.quetoquenana.pedalpal.service.ProductPackageService;
import com.quetoquenana.pedalpal.service.SystemCodeService;
import com.quetoquenana.pedalpal.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.quetoquenana.pedalpal.util.Constants.SystemCodes.GENERAL_STATUS;
import static com.quetoquenana.pedalpal.util.Constants.SystemCodes.GENERAL_STATUS_ACTIVE;

@Service
@RequiredArgsConstructor
public class ProductPackageServiceImpl implements ProductPackageService {

    private final ProductPackageRepository packageRepository;
    private final ProductRepository productRepository;
    private final SystemCodeService systemCodeService;

    @Override
    public Page<ProductPackage> findAll(Pageable pageable) {
        return packageRepository.findAll(pageable);
    }

    @Override
    public ProductPackage findById(UUID id) {
        return packageRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("package.not.found", id));
    }

    @Override
    @Transactional
    public ProductPackage createPackage(CreateProductPackageRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        SystemCode status;
        if (request.getStatus() != null) {
            status = systemCodeService.findByCategoryAndCode(GENERAL_STATUS, request.getStatus())
                    .orElseThrow(() -> new RecordNotFoundException("package.status.not.found", request.getStatus()));
        } else {
            status = systemCodeService.findByCategoryAndCode(GENERAL_STATUS, GENERAL_STATUS_ACTIVE)
                    .orElseThrow(() -> new RecordNotFoundException("package.status.not.found", GENERAL_STATUS_ACTIVE));
        }

        Set<Product> products = new HashSet<>();
        if (request.getProductIds() != null && !request.getProductIds().isEmpty()) {
            products = request.getProductIds().stream()
                    .map(pid -> productRepository.findById(pid).orElseThrow(() -> new RecordNotFoundException("product.not.found", pid)))
                    .collect(Collectors.toSet());
        }

        ProductPackage pkg = ProductPackage.createFromRequest(request, status, products);
        LocalDateTime now = LocalDateTime.now();
        pkg.setCreatedAt(now);
        pkg.setUpdatedAt(now);
        pkg.setCreatedBy(user.username());
        pkg.setUpdatedBy(user.username());

        return packageRepository.save(pkg);
    }

    @Override
    @Transactional
    public ProductPackage updatePackage(UUID id, UpdateProductPackageRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        ProductPackage existing = packageRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("package.not.found", id));

        SystemCode status = null;
        if (request.getStatus() != null) {
            status = systemCodeService.findByCategoryAndCode(GENERAL_STATUS, request.getStatus())
                    .orElseThrow(() -> new RecordNotFoundException("package.status.not.found", request.getStatus()));
        }

        Set<Product> products = null;
        if (request.getProductIds() != null) {
            products = request.getProductIds().stream()
                    .map(pid -> productRepository.findById(pid).orElseThrow(() -> new RecordNotFoundException("product.not.found", pid)))
                    .collect(Collectors.toSet());
        }

        existing.updateFromRequest(request, status, products);
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setUpdatedBy(user.username());

        return packageRepository.save(existing);
    }

    @Override
    @Transactional
    public void deletePackage(UUID id) {
        ProductPackage existing = packageRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("package.not.found", id));
        packageRepository.delete(existing);
    }

    @Override
    public List<ProductPackage> findByStatus(String statusCode) {
        return packageRepository.findByStatusCode(statusCode);
    }

    @Override
    @Transactional
    public ProductPackage addProduct(UUID packageId, UUID productId) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        ProductPackage pkg = packageRepository.findById(packageId).orElseThrow(() -> new RecordNotFoundException("package.not.found", packageId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RecordNotFoundException("product.not.found", productId));

        if (pkg.getProducts() == null) pkg.setProducts(new HashSet<>());
        pkg.getProducts().add(product);
        pkg.setUpdatedAt(LocalDateTime.now());
        pkg.setUpdatedBy(user.username());

        return packageRepository.save(pkg);
    }

    @Override
    @Transactional
    public ProductPackage removeProduct(UUID packageId, UUID productId) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        ProductPackage pkg = packageRepository.findById(packageId).orElseThrow(() -> new RecordNotFoundException("package.not.found", packageId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RecordNotFoundException("product.not.found", productId));

        if (pkg.getProducts() != null) pkg.getProducts().remove(product);
        pkg.setUpdatedAt(LocalDateTime.now());
        pkg.setUpdatedBy(user.username());

        return packageRepository.save(pkg);
    }
}

