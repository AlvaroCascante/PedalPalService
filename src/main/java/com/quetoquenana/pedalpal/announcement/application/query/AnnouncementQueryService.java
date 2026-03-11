package com.quetoquenana.pedalpal.announcement.application.query;

import com.quetoquenana.pedalpal.announcement.application.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.common.application.result.MediaResult;
import com.quetoquenana.pedalpal.common.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.media.application.port.MediaLookupPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AnnouncementQueryService {

    private final AnnouncementMapper mapper;
    private final AnnouncementRepository repository;
    private final MediaLookupPort mediaLookupPort;

    public AnnouncementResult getById(UUID id) {
        Announcement model = repository.getById(id)
                .orElseThrow(() -> new RecordNotFoundException("announcement.not.found"));

        List<MediaResult> mediaResults = mediaLookupPort.findByReferenceId(id, MediaReferenceType.ANNOUNCEMENT);

        return mapper.toResult(
                model,
                mediaResults
        );
    }

    public List<AnnouncementResult> getActive() {
        return repository.getActive().stream()
                .map(model -> {
                    List<MediaResult> mediaResults = mediaLookupPort.findByReferenceId(model.getId(), MediaReferenceType.ANNOUNCEMENT);
                    return mapper.toResult(model, mediaResults);
                }).toList();
    }
}
