package com.quetoquenana.pedalpal.product.application.query;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.product.application.mapper.ProductMapper;
import com.quetoquenana.pedalpal.product.application.result.ProductPackageResult;
import com.quetoquenana.pedalpal.product.application.result.ProductResult;
import com.quetoquenana.pedalpal.product.domain.model.Product;
import com.quetoquenana.pedalpal.product.domain.model.ProductPackage;
import com.quetoquenana.pedalpal.product.domain.repository.ProductPackageRepository;
import com.quetoquenana.pedalpal.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final ProductPackageRepository productPackageRepository;

    public ProductResult getProductById(UUID id) {
        Product model = repository.getById(id)
                .orElseThrow(RecordNotFoundException::new);

        return mapper.toProductResult(model);
    }

    public ProductPackageResult getProductPackageById(UUID id) {
        ProductPackage model = productPackageRepository.getById(id)
                .orElseThrow(RecordNotFoundException::new);

        return mapper.toProductPackageResult(model);
    }

    public List<ProductResult> getActiveProducts() {
        List<Product> models = repository.findByStatus(GeneralStatus.ACTIVE);

        return models.stream()
                .map(mapper::toProductResult)
                .toList();
    }
    public List<ProductPackageResult> getActiveProductPackages() {
        List<ProductPackage> models = productPackageRepository.findByStatus(GeneralStatus.ACTIVE);

        return models.stream()
                .map(mapper::toProductPackageResult)
                .toList();
    }
}
