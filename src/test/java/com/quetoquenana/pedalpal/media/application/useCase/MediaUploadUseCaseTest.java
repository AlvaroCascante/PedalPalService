package com.quetoquenana.pedalpal.media.application.useCase;

import com.quetoquenana.pedalpal.common.application.command.UploadMediaCommand;
import com.quetoquenana.pedalpal.common.application.command.UploadMediaSpecCommand;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.application.result.UploadMediaResult;
import com.quetoquenana.pedalpal.media.application.mapper.MediaMapper;
import com.quetoquenana.pedalpal.media.application.model.SignedUrl;
import com.quetoquenana.pedalpal.media.application.port.OwnershipValidationPort;
import com.quetoquenana.pedalpal.media.application.port.StorageProvider;
import com.quetoquenana.pedalpal.media.domain.model.Media;
import com.quetoquenana.pedalpal.media.domain.model.MediaReferenceType;
import com.quetoquenana.pedalpal.media.domain.model.MediaStatus;
import com.quetoquenana.pedalpal.media.domain.repository.MediaRepository;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.domain.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MediaUploadUseCaseTest {

    private static final String DEFAULT_PROVIDER = "r2";

    @Mock
    MediaRepository repository;

    @Mock
    MediaMapper mapper;

    @Mock
    StorageProvider storageProvider;

    @Mock
    OwnershipValidationPort ownershipValidationPort;

    @Mock
    AuthenticatedUserPort authenticatedUserPort;

    @Captor
    ArgumentCaptor<Media> mediaCaptor;

    MediaUploadUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new MediaUploadUseCase(
                repository,
                mapper,
                storageProvider,
                ownershipValidationPort,
                authenticatedUserPort,
                DEFAULT_PROVIDER
        );
    }

    @Test
    void shouldGenerateUploadUrlsAndPersistMedia() {
        UUID authUserId = UUID.randomUUID();
        UUID referenceId = UUID.randomUUID();

        when(authenticatedUserPort.getAuthenticatedUser())
                .thenReturn(Optional.of(new AuthenticatedUser(authUserId, "test-user", "Test User", UserType.CUSTOMER)));

        UploadMediaSpecCommand spec1 = new UploadMediaSpecCommand(
                "image",
                "image/jpeg",
                true,
                "front",
                "Front view"
        );
        UploadMediaSpecCommand spec2 = new UploadMediaSpecCommand(
                "image",
                "image/png",
                false,
                "side",
                "Side view"
        );

        UploadMediaCommand command = new UploadMediaCommand(
                false,
                referenceId,
                MediaReferenceType.BIKE,
                Set.of(spec1, spec2)
        );

        Media media1 = Media.builder()
                .id(UUID.randomUUID())
                .storageKey("media/key-1")
                .contentType("image/jpeg")
                .status(MediaStatus.DRAFT)
                .build();
        Media media2 = Media.builder()
                .id(UUID.randomUUID())
                .storageKey("media/key-2")
                .contentType("image/png")
                .status(MediaStatus.DRAFT)
                .build();

        SignedUrl signedUrl1 = new SignedUrl("https://upload/1", Instant.now().plusSeconds(60), Map.of("Content-Type", "image/jpeg"));
        SignedUrl signedUrl2 = new SignedUrl("https://upload/2", Instant.now().plusSeconds(60), Map.of("Content-Type", "image/png"));

        UploadMediaResult result1 = new UploadMediaResult(media1.getId(), signedUrl1.url(), media1.getStorageKey(), signedUrl1.expiresAt());
        UploadMediaResult result2 = new UploadMediaResult(media2.getId(), signedUrl2.url(), media2.getStorageKey(), signedUrl2.expiresAt());

        when(mapper.toModel(eq(authUserId), eq(command), eq(spec1))).thenReturn(media1);
        when(mapper.toModel(eq(authUserId), eq(command), eq(spec2))).thenReturn(media2);
        when(storageProvider.generateUploadUrl(media1.getStorageKey(), media1.getContentType(), false)).thenReturn(signedUrl1);
        when(storageProvider.generateUploadUrl(media2.getStorageKey(), media2.getContentType(), false)).thenReturn(signedUrl2);
        when(mapper.toResult(any(Media.class), eq(signedUrl1))).thenReturn(result1);
        when(mapper.toResult(any(Media.class), eq(signedUrl2))).thenReturn(result2);

        Set<UploadMediaResult> results = useCase.execute(command);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.containsAll(Set.of(result1, result2)));

        verify(ownershipValidationPort, times(1)).validate(
                MediaReferenceType.BIKE,
                referenceId
        );
        verify(storageProvider, times(1)).generateUploadUrl(media1.getStorageKey(), media1.getContentType(), false);
        verify(storageProvider, times(1)).generateUploadUrl(media2.getStorageKey(), media2.getContentType(), false);
        verify(repository, times(2)).save(mediaCaptor.capture());

        List<Media> persisted = mediaCaptor.getAllValues();
        assertEquals(2, persisted.size());
        assertTrue(persisted.stream().allMatch(media -> DEFAULT_PROVIDER.equals(media.getProvider())));
    }
}
