package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.common.domain.model.Store;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.StoreEntity;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public Store toDomain(StoreEntity entity) {
        if (entity == null) return null;

        Store domain = Store.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();

        return domain;
    }

    public StoreEntity toEntity(Store domain) {
        if (domain == null) return null;

        StoreEntity entity = StoreEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .build();
        return entity;
    }
}

