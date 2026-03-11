package com.quetoquenana.pedalpal.media.application.query;

import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.media.application.mapper.MediaMapper;
import com.quetoquenana.pedalpal.media.application.model.SignedUrl;
import com.quetoquenana.pedalpal.media.application.port.MediaUrlProvider;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.common.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import lombok.RequiredArgsConstructor;

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
    private final MediaUrlProvider mediaUrlProvider;

    /**
     * Retrieves a store by id.
     */
    public MediaResult getById(UUID id) {
        Media model = repository.getById(id)
                .orElseThrow(RecordNotFoundException::new);


        SignedUrl signedUrl = mediaUrlProvider.generateDownloadUrl(
                model.getStorageKey(),
                model.getContentType().name(),
                model.getReferenceType().isPublic()
        );
        return mapper.toResult(model, signedUrl.url(), signedUrl.expiresAt());
    }

    /**
     * Retrieves a MediaResults ReferenceId And ReferenceType.
     */
    public List<MediaResult> getByReferenceIdAndReferenceType(UUID id, MediaReferenceType referenceType) {
        List<Media> models = repository.findByReferenceIdAndReferenceType(id, referenceType);

        List<MediaResult> results = new ArrayList<>();
        models.forEach( model -> {
            if (model.getStatus() == MediaStatus.ACTIVE) {
                SignedUrl signedUrl = mediaUrlProvider.generateDownloadUrl(
                        model.getStorageKey(),
                        model.getContentType().name(),
                        referenceType.isPublic()
                );
                results.add(mapper.toResult(model, signedUrl.url(), signedUrl.expiresAt()));
            }
        });
        return results;
    }
}
