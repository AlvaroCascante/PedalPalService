package com.quetoquenana.pedalpal.announcement.usecase;

import com.quetoquenana.pedalpal.announcement.application.command.UpdateAnnouncementStatusCommand;
import com.quetoquenana.pedalpal.announcement.application.result.AnnouncementResult;
import com.quetoquenana.pedalpal.announcement.application.usecase.UpdateAnnouncementStatusUseCase;
import com.quetoquenana.pedalpal.announcement.domain.model.Announcement;
import com.quetoquenana.pedalpal.announcement.domain.repository.AnnouncementRepository;
import com.quetoquenana.pedalpal.announcement.mapper.AnnouncementMapper;
import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.util.TestAnnouncementData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAnnouncementStatusUseCaseTest {

    @Mock
    AnnouncementRepository repository;

    @Mock
    AnnouncementMapper mapper;

    @InjectMocks
    UpdateAnnouncementStatusUseCase useCase;

    @Captor
    ArgumentCaptor<Announcement> announcementCaptor;

    @Nested
    class HappyPath {

        @Test
        void shouldUpdateStatus_andPersist() {
            UUID id = UUID.randomUUID();
            Announcement existing = TestAnnouncementData.existingAnnouncement(id);
            when(repository.getById(id)).thenReturn(Optional.of(existing));

            Announcement saved = TestAnnouncementData.existingAnnouncement(id);
            when(repository.save(any(Announcement.class))).thenReturn(saved);

            AnnouncementResult result = TestAnnouncementData.result(id);
            when(mapper.toResult(saved)).thenReturn(result);

            UpdateAnnouncementStatusCommand command = TestAnnouncementData.statusCommand(id, "INACTIVE");

            useCase.execute(command);

            verify(repository, times(1)).save(announcementCaptor.capture());
            assertEquals(GeneralStatus.INACTIVE, announcementCaptor.getValue().getStatus());
        }
    }

    @Nested
    class ValidationAndGuards {

        @Test
        void shouldThrowNotFound_whenAnnouncementDoesNotExist() {
            UUID id = UUID.randomUUID();
            when(repository.getById(id)).thenReturn(Optional.empty());

            assertThrows(RecordNotFoundException.class, () -> useCase.execute(TestAnnouncementData.statusCommand(id, "ACTIVE")));
            verify(repository, never()).save(any());
        }

        @Test
        void shouldThrowBadRequest_whenStatusIsBlank() {
            UUID id = UUID.randomUUID();
            when(repository.getById(id)).thenReturn(Optional.of(TestAnnouncementData.existingAnnouncement(id)));

            BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(TestAnnouncementData.statusCommand(id, "  ")));
            assertEquals("announcement.update.status.required", ex.getMessage());
        }
    }
}

