package com.quetoquenana.pedalpal.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import static com.quetoquenana.pedalpal.common.util.Constants.MessageSource.BASE_NAME;
import static com.quetoquenana.pedalpal.common.util.Constants.MessageSource.DEFAULT_ENCODING;

@Configuration
public class I18nConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(BASE_NAME);
        messageSource.setDefaultEncoding(DEFAULT_ENCODING);
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver();
    }
}
