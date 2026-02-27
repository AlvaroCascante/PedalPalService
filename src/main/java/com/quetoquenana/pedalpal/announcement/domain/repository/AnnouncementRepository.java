package com.quetoquenana.pedalpal.announcement.domain.repository;

import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnnouncementRepository {
    Announcement save(Announcement announcement);

    Optional<Announcement> getById(UUID id);

    List<Announcement> getActive();
}
