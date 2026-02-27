package com.quetoquenana.pedalpal.announcement.application.query;

import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AnnouncementQueryService {

    private final AnnouncementMapper mapper;
    private final AnnouncementRepository repository;

    public AnnouncementResult getById(UUID id) {
        Announcement model = repository.getById(id)
                .orElseThrow(() -> new RecordNotFoundException("announcement.not.found"));
        return mapper.toResult(model);
    }

    public List<AnnouncementResult> getActive() {
        return repository.getActive().stream()
                .map(mapper::toResult)
                .toList();
    }
}
