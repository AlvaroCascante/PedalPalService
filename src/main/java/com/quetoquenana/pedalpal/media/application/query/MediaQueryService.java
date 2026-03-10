package com.quetoquenana.pedalpal.media.application.query;

import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.media.application.mapper.MediaMapper;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Read-only query service for media data.
 */
@RequiredArgsConstructor
public class MediaQueryService {

    private final MediaMapper mapper;
    private final MediaRepository repository;

    @Value("${app.cdn.base-url:}")
    private String cdnBaseUrl;

    /**
     * Retrieves a store by id.
     */
    public MediaResult getById(UUID id) {
        Media model = repository.getById(id)
                .orElseThrow(RecordNotFoundException::new);

        String publicUrl = providePublicCdnUrl(model.getStorageKey());
        return mapper.toResult(model, publicUrl);
    }

    /**
     * Retrieves a MediaResults ReferenceId And ReferenceType.
     */
    public List<MediaResult> getByReferenceIdAndReferenceType(UUID id, String referenceType) {
        List<Media> models = repository.findByReferenceIdAndReferenceType(id, MediaReferenceType.from(referenceType));

        List<MediaResult> results = new ArrayList<>();
        models.forEach( model -> {
            if (model.getStatus() == MediaStatus.ACTIVE) {
                String publicUrl = providePublicCdnUrl(model.getStorageKey());
                results.add(mapper.toResult(model, publicUrl));
            }
        });
        return results;
    }

    public String providePublicCdnUrl(String storageKey) {
        return cdnBaseUrl + "/" + storageKey;
    }
}
