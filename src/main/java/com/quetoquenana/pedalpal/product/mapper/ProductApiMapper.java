package com.quetoquenana.pedalpal.product.mapper;

import com.quetoquenana.pedalpal.product.application.result.ProductPackageResult;
import com.quetoquenana.pedalpal.product.application.result.ProductResult;
import com.quetoquenana.pedalpal.product.presentation.dto.response.ProductPackageResponse;
import com.quetoquenana.pedalpal.product.presentation.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductApiMapper {

    private final MessageSource messageSource;

    public ProductResponse toResponse(ProductResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        return new ProductResponse(
                result.id(),
                result.name(),
                result.description(),
                result.price(),
                statusLabel
        );
    }

    public ProductPackageResponse toResponse(ProductPackageResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        return new ProductPackageResponse(
                result.id(),
                result.name(),
                result.description(),
                result.price(),
                statusLabel,
                result.products().stream().map(this::toResponse).collect(Collectors.toSet())
        );
    }
}
