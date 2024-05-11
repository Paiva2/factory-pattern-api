package com.root.pattern.domain.usecase.musician;

import com.root.pattern.adapter.dto.musician.FilterMusicianOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.musician.FilterMusicianUsecase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FilterMusicianUsecaseImplTest {
    @Mock
    private MusicianDataProvider musicianDataProvider;

    private FilterMusicianUsecase sut;

    private Music musicBuilder() {
        return Music.builder().build();
    }

    private Album albumBuilder(Music music) {
        return Album.builder()
            .id(UUID.randomUUID())
            .name("any_name")
            .createdAt(new Date())
            .music(Collections.singletonList(music))
            .build();
    }

    private Musician musicianBuilder(Album album) {
        return Musician.builder()
            .id(1L)
            .name("any_name")
            .email("any_email")
            .role(Role.MUSICIAN)
            .createdAt(new Date())
            .albums(Collections.singletonList(album))
            .build();
    }

    @BeforeEach
    public void setup() {
        this.sut = FilterMusicianUsecaseImpl.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .build();

        Music music = this.musicBuilder();
        Album album = this.albumBuilder(music);
        Musician musician = this.musicianBuilder(album);

        Mockito.when(this.musicianDataProvider.findById(Mockito.any())).thenReturn(Optional.ofNullable(musician));
        Mockito.when(this.musicianDataProvider.findByName(Mockito.any())).thenReturn(Optional.ofNullable(musician));
    }

    @Test
    public void shouldCallSutWithCorrectProvidedParams() {
        FilterMusicianUsecase mockSut = Mockito.mock(FilterMusicianUsecaseImpl.class);
        mockSut.exec(1L, "any_name");

        Mockito.verify(mockSut, Mockito.times(1)).exec(1L, "any_name");
    }

    @Test
    public void shouldThrowExceptionIfBothParametersAreNotProvided() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(null, null);
        });

        Assertions.assertEquals("Musician id and name can't be empty", exception.getMessage());
    }

    @Test
    public void shouldGetByIdIfBothParametersAreProvided() {
        this.sut.exec(1L, "any_name");

        Mockito.verify(this.musicianDataProvider, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    public void shouldGetByIdIfIdIsProvidedOnly() {
        this.sut.exec(1L, null);

        Mockito.verify(this.musicianDataProvider, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    public void shouldGetByIdIfNameIsProvidedOnly() {
        this.sut.exec(null, "any_name");

        Mockito.verify(this.musicianDataProvider, Mockito.times(1)).findByName(Mockito.anyString());
    }

    @Test
    public void shouldThrowExceptionIfMusicianDontExists() {
        Mockito.when(this.musicianDataProvider.findById(Mockito.any())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(1L, null);
        });

        Assertions.assertEquals("Musician not found", exception.getMessage());
    }

    @Test
    void shouldThrowErrorIfMusicianIsDisabled() {
        Musician musician = this.musicianBuilder(null);
        musician.setDisabled(true);

        Mockito.when(this.musicianDataProvider.findById(Mockito.any()))
            .thenReturn(Optional.of(musician));

        ForbiddenException exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            this.sut.exec(1L, null);
        });

        Assertions.assertEquals("Musician is disabled", exception.getMessage());
    }

    @Test
    public void shouldReturnSutOutputIfNothingGoesWrong() {
        FilterMusicianOutputDTO sutOutput = this.sut.exec(1L, null);

        Assertions.assertAll("Sut output",
            () -> Assertions.assertNotNull(sutOutput.getId()),
            () -> Assertions.assertEquals("any_name", sutOutput.getName()),
            () -> Assertions.assertEquals("any_email", sutOutput.getEmail()),
            () -> Assertions.assertEquals(Role.MUSICIAN, sutOutput.getRole()),
            () -> Assertions.assertNotNull(sutOutput.getCreatedAt()),
            () -> Assertions.assertNotNull(sutOutput.getAlbums()),
            () -> Assertions.assertFalse(sutOutput.getAlbums().isEmpty())
        );
    }
}