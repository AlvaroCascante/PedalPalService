package com.quetoquenana.pedalpal.service.impl;

import com.quetoquenana.pedalpal.dto.api.request.CreateSuggestionRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateSuggestionRequest;
import com.quetoquenana.pedalpal.dto.util.SecurityUser;
import com.quetoquenana.pedalpal.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.model.data.Bike;
import com.quetoquenana.pedalpal.model.data.Product;
import com.quetoquenana.pedalpal.model.data.ProductPackage;
import com.quetoquenana.pedalpal.model.data.Suggestion;
import com.quetoquenana.pedalpal.model.data.SystemCode;
import com.quetoquenana.pedalpal.repository.BikeRepository;
import com.quetoquenana.pedalpal.repository.ProductPackageRepository;
import com.quetoquenana.pedalpal.repository.ProductRepository;
import com.quetoquenana.pedalpal.repository.SuggestionRepository;
import com.quetoquenana.pedalpal.service.SuggestionService;
import com.quetoquenana.pedalpal.service.SystemCodeService;
import com.quetoquenana.pedalpal.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.quetoquenana.pedalpal.util.Constants.SystemCodes.SUGGESTION_TYPE;

@Service
@RequiredArgsConstructor
public class SuggestionServiceImpl implements SuggestionService {

    private final SuggestionRepository repository;
    private final BikeRepository bikeRepository;
    private final SystemCodeService systemCodeService;
    private final ProductPackageRepository packageRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<Suggestion> findAll(Pageable pageable) {
        Page<Suggestion> page = repository.findAll(pageable);
        return new PageImpl<>(page.getContent(), pageable, page.getTotalElements());
    }

    @Override
    public Suggestion findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException("suggestion.not.found", id));
    }

    @Override
    @Transactional
    public Suggestion createSuggestion(CreateSuggestionRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        Bike bike = bikeRepository.findById(request.getBikeId()).orElseThrow(() -> new RecordNotFoundException("bike.not.found", request.getBikeId()));

        SystemCode suggestionType = systemCodeService.findByCategoryAndCode("SUGGESTION_TYPE", request.getSuggestionType())
                .orElseThrow(() -> new RecordNotFoundException("suggestion.type.not.found", request.getSuggestionType()));

        ProductPackage productPackage = fetchPackageIfExists(request.getPackageId());
        Product product = fetchProductIfExists(request.getProductId());

        Suggestion suggestion = Suggestion.createFromRequest(request, bike, suggestionType, productPackage, product);
        LocalDateTime now = LocalDateTime.now();
        suggestion.setCreatedAt(now);
        suggestion.setUpdatedAt(now);
        suggestion.setCreatedBy(user.username());
        suggestion.setUpdatedBy(user.username());

        return repository.save(suggestion);
    }

    @Override
    @Transactional
    public Suggestion updateSuggestion(UUID id, UpdateSuggestionRequest request) {
        SecurityUser user = SecurityUtils.getCurrentUser().orElseThrow(
                () -> new ForbiddenAccessException("authentication.required")
        );

        Suggestion existing = repository.findById(id).orElseThrow(() -> new RecordNotFoundException("suggestion.not.found", id));

        Bike bike = null;
        SystemCode suggestionType = null;
        ProductPackage productPackage = fetchPackageIfExists(request.getPackageId());
        Product product = fetchProductIfExists(request.getProductId());

        if (request.getBikeId() != null) {
            bike = bikeRepository.findById(request.getBikeId()).orElseThrow(() -> new RecordNotFoundException("bike.not.found", request.getBikeId()));
        }
        if (request.getSuggestionType() != null) {
            suggestionType = systemCodeService.findByCategoryAndCode(SUGGESTION_TYPE, request.getSuggestionType())
                    .orElseThrow(() -> new RecordNotFoundException("suggestion.type.not.found", request.getSuggestionType()));
        }

        existing.updateFromRequest(request, bike, suggestionType, productPackage, product);
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setUpdatedBy(user.username());

        return repository.save(existing);
    }

    @Override
    @Transactional
    public void deleteSuggestion(UUID id) {
        Suggestion existing = repository.findById(id).orElseThrow(() -> new RecordNotFoundException("suggestion.not.found", id));
        repository.delete(existing);
    }

    @Override
    public List<Suggestion> findByBikeId(UUID bikeId) {
        return repository.findByBikeId(bikeId);
    }

    @Override
    public List<Suggestion> findBySuggestionType(String code) {
        return repository.findBySuggestionTypeCode(code);
    }

    @Override
    public List<Suggestion> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Suggestion> findByDateRange(LocalDate start, LocalDate end) {
        return repository.findBySuggestedDateBetween(start, end);
    }

    private ProductPackage fetchPackageIfExists(UUID packageId) {
        if (packageId != null) {
            return packageRepository.findById(packageId).orElseThrow(() -> new RecordNotFoundException("package.not.found", packageId));
        }
        return null;
    }

    private Product fetchProductIfExists(UUID productId) {
        if (productId != null) {
            return productRepository.findById(productId).orElseThrow(() -> new RecordNotFoundException("product.not.found", productId));
        }
        return null;
    }
}

