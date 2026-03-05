package com.quetoquenana.pedalpal.systemCode.presentation.mapper;

import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;
import com.quetoquenana.pedalpal.systemCode.presentation.dto.response.SystemCodeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Maps SystemCode results to API responses with localized labels.
 */
@Component
@RequiredArgsConstructor
public class SystemCodeApiMapper {

    private final MessageSource messageSource;

    /**
     * Builds a localized API response from the query result.
     */
    public SystemCodeResponse toResponse(SystemCodeResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String codeDescription = messageSource.getMessage(result.codeKey(), null, locale);
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        return new SystemCodeResponse(
                result.id(),
                result.category(),
                result.code(),
                codeDescription,
                statusLabel,
                result.position()
        );
    }
}
