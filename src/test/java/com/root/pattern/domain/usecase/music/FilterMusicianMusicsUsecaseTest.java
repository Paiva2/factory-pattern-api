package com.root.pattern.domain.usecase.music;

import com.root.pattern.adapter.dto.music.ListFilterMusicOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.enums.MusicCategory;
import com.root.pattern.domain.enums.Role;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FilterMusicianMusicsUsecaseTest {
    @Mock
    private MusicianDataProvider musicianDataProvider;

    @Mock
    private MusicDataProvider musicDataProvider;

    private FilterMusicianMusicsUsecase sut;

    private Category categoryBuilder() {
        return Category.builder()
            .id(UUID.randomUUID())
            .name(MusicCategory.ROCK)
            .createdAt(new Date())
            .updatedAt(new Date())
            .build();
    }

    private Album albumBuilder() {
        return Album.builder()
            .id(UUID.randomUUID())
            .name("any_name")
            .createdAt(new Date())
            .build();
    }

    private Music musicBuilder(UUID id, boolean single, Album album, Musician musician) {
        return Music.builder()
            .id(id)
            .isSingle(single)
            .duration(2000L)
            .name("any_name")
            .createdAt(new Date())
            .album(album)
            .category(this.categoryBuilder())
            .musician(musician)
            .build();
    }

    private Musician musicianBuilder() {
        return Musician.builder()
            .id(1L)
            .name("any_name")
            .email("any_email")
            .role(Role.MUSICIAN)
            .createdAt(new Date())
            .disabled(false)
            .updatedAt(new Date())
            .build();
    }

    @BeforeEach
    public void setup() {
        this.sut = FilterMusicianMusicsUsecase.builder()
            .musicianDataProvider(this.musicianDataProvider)
            .musicDataProvider(this.musicDataProvider)
            .build();

        Musician musicianMock = this.musicianBuilder();
        Album albumMock = this.albumBuilder();
        Music musicMockOne = this.musicBuilder(UUID.randomUUID(), true, null, musicianMock);
        Music musicMockTwo = this.musicBuilder(UUID.randomUUID(), false, albumMock, musicianMock);
        albumMock.setMusic(Arrays.asList(musicMockOne, musicMockTwo));

        Mockito.when(this.musicianDataProvider.findById(Mockito.any())).thenReturn(Optional.ofNullable(musicianMock));
        Mockito.when(this.musicDataProvider.findAllByMusician(Mockito.any(), Mockito.any())).thenReturn((new PageImpl<>(Arrays.asList(musicMockOne, musicMockTwo))));
    }

    @Test
    public void shouldCallSutWithCorrectlyProvidedParams() {
        FilterMusicianMusicsUsecase mockSut = Mockito.mock(FilterMusicianMusicsUsecase.class);
        mockSut.exec(1L, 1, 5);

        Mockito.verify(mockSut, Mockito.times(1)).exec(1L, 1, 5);
    }

    @Test
    public void shouldThrowExceptionIfMusicianIdIsEmpty() {
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            this.sut.exec(null, 1, 5);
        });

        Assertions.assertEquals("Musician id can't be empty", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfMusicianDontExists() {
        Mockito.when(this.musicianDataProvider.findById(Mockito.any())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            this.sut.exec(1L, 1, 5);
        });

        Assertions.assertEquals("Musician not found", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfMusicianIsDisabled() {
        Musician musician = this.musicianBuilder();
        musician.setDisabled(true);

        Mockito.when(this.musicianDataProvider.findById(Mockito.any())).thenReturn(Optional.of(musician));

        ForbiddenException exception = Assertions.assertThrows(ForbiddenException.class, () -> {
            this.sut.exec(1L, 1, 5);
        });

        Assertions.assertEquals("Musician is disabled", exception.getMessage());
    }

    @Test
    public void shouldCallRepositoryWithPageOneIfPageZeroIsProvided() {
        int page = 0;
        int perPage = 5;

        this.sut.exec(1L, page, perPage);

        Pageable pageable = PageRequest.of(0, perPage, Sort.Direction.DESC, "createdAt");

        Mockito.verify(this.musicDataProvider, Mockito.times(1)).findAllByMusician(pageable, 1L);
    }

    @Test
    public void shouldCallRepositoryWithPerPageFiveIfLessIsProvided() {
        int page = 1;
        int perPage = 4;

        this.sut.exec(1L, page, perPage);

        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "createdAt");

        Mockito.verify(this.musicDataProvider, Mockito.times(1)).findAllByMusician(pageable, 1L);
    }

    @Test
    public void shouldCallRepositoryWithPerPageFifthIfMoreIsProvided() {
        int page = 1;
        int perPage = 51;

        this.sut.exec(1L, page, perPage);

        Pageable pageable = PageRequest.of(0, 50, Sort.Direction.DESC, "createdAt");

        Mockito.verify(this.musicDataProvider, Mockito.times(1)).findAllByMusician(pageable, 1L);
    }

    @Test
    public void shouldReturnSutOutputIfNothingGoesWrong() {
        ListFilterMusicOutputDTO outputSut = this.sut.exec(1L, 1, 5);

        Assertions.assertAll("Sut output assertions",
            () -> Assertions.assertEquals(1, outputSut.getPage()),
            () -> Assertions.assertEquals(5, outputSut.getPerPage()),
            () -> Assertions.assertEquals(2, outputSut.getTotalItems()),
            () -> Assertions.assertFalse(outputSut.getMusics().isEmpty()),
            () -> Assertions.assertNotNull(outputSut.getMusics().get(1).getAlbum()),
            () -> Assertions.assertNotNull(outputSut.getMusics().get(1).getId()),
            () -> Assertions.assertEquals("any_name", outputSut.getMusics().get(1).getName()),
            () -> Assertions.assertNotNull(outputSut.getMusics().get(1).getCategory()),
            () -> Assertions.assertNotNull(outputSut.getMusics().get(1).getMusician())
        );
    }


}