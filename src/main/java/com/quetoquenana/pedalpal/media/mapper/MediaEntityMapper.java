package com.quetoquenana.pedalpal.media.mapper;

import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.infrastructure.persistence.entity.MediaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MediaEntityMapper {

    public Media toModel(MediaEntity entity) {
        return Media.builder()
                .id(entity.getId())
                .ownerId(entity.getOwnerId())
                .referenceId(entity.getReferenceId())
                .referenceType(entity.getReferenceType())
                .mediaType(entity.getMediaType())
                .contentType(entity.getContentType())
                .isPrimary(entity.getIsPrimary())
                .status(entity.getStatus())
                .storageKey(entity.getStorageKey())
                .provider(entity.getProvider())
                .providerAssetId(entity.getProviderAssetId())
                .sizeBytes(entity.getSizeBytes())
                .metadata(entity.getMetadata())
                .title(entity.getTitle())
                .altText(entity.getAltText())
                .build();
    }

    public MediaEntity toEntity(Media model) {
        return MediaEntity.builder()
                .id(model.getId())
                .ownerId(model.getOwnerId())
                .referenceId(model.getReferenceId())
                .referenceType(model.getReferenceType())
                .mediaType(model.getMediaType())
                .contentType(model.getContentType())
                .isPrimary(model.getIsPrimary())
                .status(model.getStatus())
                .storageKey(model.getStorageKey())
                .provider(model.getProvider())
                .providerAssetId(model.getProviderAssetId())
                .sizeBytes(model.getSizeBytes())
                .metadata(model.getMetadata())
                .title(model.getTitle())
                .altText(model.getAltText())
                .build();
    }
}
