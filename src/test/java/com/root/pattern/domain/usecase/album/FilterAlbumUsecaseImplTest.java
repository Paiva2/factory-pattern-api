package com.root.pattern.domain.usecase.album;

import com.root.pattern.adapter.dto.album.FilterAlbumOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.MusicCategory;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.usecase.FilterAlbumUsecase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FilterAlbumUsecaseImplTest {
    @Mock
    private AlbumDataProvider albumDataProvider;

    private FilterAlbumUsecase sut;

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
        this.sut = FilterAlbumUsecaseImpl.builder()
            .albumDataProvider(this.albumDataProvider)
            .build();

        Category categoryMock = this.categoryBuilder();
        Music musicMock = this.musicBuilder(categoryMock);
        Musician musicianMock = this.musicianBuilder();
        Album albumMock = this.albumBuilder(musicMock, musicianMock);

        Mockito.when(this.albumDataProvider.findById(Mockito.any())).thenReturn(Optional.ofNullable(albumMock));
    }

    @Test
    public void shouldCallSutWithCorrectlyProvidedParams() {
        UUID randomAlbumId = UUID.randomUUID();

        FilterAlbumUsecase mockSut = Mockito.mock(FilterAlbumUsecaseImpl.class);
        mockSut.exec(randomAlbumId);

        Mockito.verify(mockSut, Mockito.times(1)).exec(randomAlbumId);
    }

    @Test
    public void shouldThrowExceptionIfAlbumIdIsNotProvided() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(null);
        });

        Assertions.assertEquals("Album id can't be empty", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfAlbumDontExists() {
        Mockito.when(this.albumDataProvider.findById(Mockito.any())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(UUID.randomUUID());
        });

        Assertions.assertEquals("Album not found", exception.getMessage());
    }

    @Test
    public void shouldReturnSutOutputIfNothingGoesWrong() {
        FilterAlbumOutputDTO sutOutput = this.sut.exec(UUID.randomUUID());

        Assertions.assertAll("Sut output assertions",
            () -> Assertions.assertNotNull(sutOutput.getId()),
            () -> Assertions.assertEquals("any_album_name", sutOutput.getName()),
            () -> Assertions.assertNotNull(sutOutput.getCreatedAt()),
            () -> Assertions.assertNotNull(sutOutput.getUpdatedAt()),
            () -> Assertions.assertEquals("any_name", sutOutput.getMusician().getName()),
            () -> Assertions.assertEquals("any_email", sutOutput.getMusician().getEmail()),
            () -> Assertions.assertEquals(Role.MUSICIAN, sutOutput.getMusician().getRole()),
            () -> Assertions.assertNotNull(sutOutput.getMusician().getId()),
            () -> Assertions.assertEquals(1, sutOutput.getMusics().size()),
            () -> Assertions.assertEquals("any_music_name", sutOutput.getMusics().get(0).getName()),
            () -> Assertions.assertNotNull(sutOutput.getMusics().get(0).getCategory())
        );
    }

}