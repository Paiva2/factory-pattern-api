package com.root.pattern.domain.usecase.album;

import com.root.pattern.adapter.dto.album.ListAllAlbumsOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.MusicCategory;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.ListMusicianAlbumsUsecase;
import org.hibernate.annotations.NotFound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ListMusicianAlbumsUsecaseImplTest {
    @Mock
    private MusicianDataProvider musicianDataProvider;

    @Mock
    private AlbumDataProvider albumDataProvider;

    private ListMusicianAlbumsUsecase sut;

    private Category categoryBuilder() {
        return Category.builder()
            .id(UUID.randomUUID())
            .name(MusicCategory.ROCK)
            .build();
    }

    private Music musicBuilder(Category category) {
        return Music.builder()
            .id(UUID.randomUUID())
            .name("any_music_name")
            .isSingle(false)
            .duration(2000L)
            .category(category)
            .createdAt(new Date())
            .build();
    }

    private Musician musicianBuilder() {
        return Musician.builder()
            .id(1L)
            .email("any_email")
            .name("any_name")
            .createdAt(new Date())
            .role(Role.MUSICIAN)
            .build();
    }

    private Album albumBuilder(Music musics, Musician musician) {
        return Album.builder()
            .id(UUID.randomUUID())
            .name("any_album_name")
            .disabled(false)
            .disabledAt(null)
            .updatedAt(new Date())
            .createdAt(new Date())
            .musician(musician)
            .music(Collections.singletonList(musics))
            .build();
    }

    @BeforeEach
    public void setup() {
        this.sut = ListMusicianAlbumsUsecaseImpl.builder()
            .albumDataProvider(this.albumDataProvider)
            .musicianDataProvider(this.musicianDataProvider)
            .build();

        Musician musicianMock = this.musicianBuilder();
        Category categoryMock = this.categoryBuilder();
        Music musicMock = this.musicBuilder(categoryMock);
        Album albumMock = this.albumBuilder(musicMock, musicianMock);

        Mockito.when(this.musicianDataProvider.findById(Mockito.any())).thenReturn(Optional.ofNullable(musicianMock));
        Mockito.when(this.albumDataProvider.findAllByMusicianId(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(Collections.singletonList(albumMock)));
        Mockito.when(this.albumDataProvider.findAllByMusicianIdAndAlbumName(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(Collections.singletonList(albumMock)));
    }

    @Test
    public void shouldCallSutWithCorrectProvidedParams() {
        ListMusicianAlbumsUsecase mockSut = Mockito.mock(ListMusicianAlbumsUsecaseImpl.class);
        mockSut.exec(1L, 1, 5, "any_album_name");

        Mockito.verify(mockSut, Mockito.times(1)).exec(1L, 1, 5, "any_album_name");
    }

    @Test
    public void shouldThrowExceptionIfMusicianIdIsEmpty() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(null, 1, 5, "any_album_name");
        });

        Assertions.assertEquals("Musician id can't be null", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfMusicianDontExists() {
        Mockito.when(this.musicianDataProvider.findById(Mockito.any())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(1L, 1, 5, "any_album_name");
        });

        Assertions.assertEquals("Musician not found", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfMusicianIsDisabled() {
        Musician musicianMock = this.musicianBuilder();
        musicianMock.setDisabled(true);

        Mockito.when(this.musicianDataProvider.findById(Mockito.any())).thenReturn(Optional.of(musicianMock));

        ForbiddenException exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            this.sut.exec(1L, 1, 5, "any_album_name");
        });

        Assertions.assertEquals("Musician disabled", exception.getMessage());
    }

    @Test
    public void shouldReturnSutOutputIfNothingGoesWrong() {
        ListAllAlbumsOutputDTO sutOutput = this.sut.exec(1L, 1, 5, "any_album_name");

        Assertions.assertAll("Output assertions",
            () -> Assertions.assertNotNull(sutOutput),
            () -> Assertions.assertEquals(1, sutOutput.getPage()),
            () -> Assertions.assertEquals(5, sutOutput.getPerPage()),
            () -> Assertions.assertEquals(1, sutOutput.getTotalAlbuns()),
            () -> Assertions.assertNotNull(sutOutput.getAlbuns()),
            () -> Assertions.assertFalse(sutOutput.getAlbuns().isEmpty())
        );
    }
}