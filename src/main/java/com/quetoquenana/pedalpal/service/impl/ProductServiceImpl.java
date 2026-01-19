package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.api.request.CreateProductRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateProductRequest;
import com.quetoquenana.pedalpal.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.data.Product;
import com.quetoquenana.pedalpal.model.data.SystemCode;
import com.quetoquenana.pedalpal.repository.ProductRepository;
import com.quetoquenana.pedalpal.service.ProductService;
import com.quetoquenana.pedalpal.service.SystemCodeService;
import com.quetoquenana.pedalpal.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.quetoquenana.pedalpal.util.Constants.SystemCodes.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SystemCodeService systemCodeService;

    public ProductServiceImpl(ProductRepository productRepository, SystemCodeService systemCodeService) {
        this.productRepository = productRepository;
        this.systemCodeService = systemCodeService;
    }

    @Override
    public Product findById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("product.not.found", productId));
    }

    @Override
    public Page<Product> findByStatusCodes(Pageable pageable, List<String> codes) {
        if (codes == null || codes.isEmpty()) return productRepository.findAll(pageable);
        return productRepository.findByStatusCodes(pageable, codes);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return this.findByStatusCodes(pageable, null);
    }

    @Override
    public Page<Product> findAllActive(Pageable pageable) {
        return this.findByStatusCodes(pageable, List.of(GENERAL_STATUS_ACTIVE));
    }

    @Override
    @Transactional
    public Product createProduct(CreateProductRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        SystemCode status;
        if (request.getStatus() != null) {
            status = systemCodeService.findByCategoryAndCode(GENERAL_STATUS, request.getStatus())
                    .orElseThrow(() -> new RecordNotFoundException("product.status.not.found", request.getStatus()));
        } else {
            status = systemCodeService.findByCategoryAndCode(GENERAL_STATUS, GENERAL_STATUS_ACTIVE)
                    .orElseThrow(() -> new RecordNotFoundException("product.status.not.found", GENERAL_STATUS_ACTIVE));
        }

        Product product = Product.createFromRequest(request, status);
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        product.setCreatedBy(user.username());
        product.setUpdatedBy(user.username());
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(UUID id, UpdateProductRequest request) {
        SystemCode status = null;
        if (request.getStatus() != null) {
            status = systemCodeService.findByCategoryAndCode(GENERAL_STATUS, request.getStatus())
                    .orElseThrow(() -> new RecordNotFoundException("product.status.not.found", request.getStatus()));
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("product.not.found", id));

        product.updateFromRequest(request, status);
        product.setUpdatedAt(LocalDateTime.now());
        // set updatedBy from current user if available
        SecurityUtils.getCurrentUser().ifPresent(u -> product.setUpdatedBy(u.username()));
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("product.not.found", id));
        SystemCode status = systemCodeService.findByCategoryAndCode(GENERAL_STATUS, GENERAL_STATUS_INACTIVE)
                    .orElseThrow(() -> new RecordNotFoundException("product.status.not.found", GENERAL_STATUS_INACTIVE));
        product.setStatus(status);
        product.setUpdatedAt(LocalDateTime.now());
        SecurityUtils.getCurrentUser().ifPresent(u -> product.setUpdatedBy(u.username()));
        productRepository.save(product);
    }

}
