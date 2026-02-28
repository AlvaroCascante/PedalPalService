package com.quetoquenana.pedalpal.media.infrastructure.cdn;

import com.quetoquenana.pedalpal.media.application.port.CdnUrlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CdnUrlProviderImpl implements CdnUrlProvider {

    @Value("${app.cdn.base-url:}")
    private String cdnBaseUrl;

    @Override
    public String providePublic(String storageKey) {
        return cdnBaseUrl + "/" + storageKey;
    }
}
