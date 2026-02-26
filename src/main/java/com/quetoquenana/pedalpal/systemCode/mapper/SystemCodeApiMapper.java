package com.quetoquenana.pedalpal.systemCode.mapper;

import com.quetoquenana.pedalpal.systemCode.application.result.SystemCodeResult;
import com.quetoquenana.pedalpal.systemCode.presentation.dto.response.SystemCodeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class SystemCodeApiMapper {

    private final MessageSource messageSource;

    public SystemCodeResponse toResponse(SystemCodeResult result) {
        Locale locale = LocaleContextHolder.getLocale();
        String name = messageSource.getMessage(result.status().getKey(), null, locale);
        String statusLabel = messageSource.getMessage(result.status().getKey(), null, locale);

        return new SystemCodeResponse(
                result.id(),
                result.code(),
                name,
                statusLabel,
                result.position()
        );
    }
}
