package com.root.pattern.domain.usecase.music;

import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.music.MusicOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.CategoryDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.strategy.CopyPropertiesStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;

//TODO: TESTS
@Builder
@AllArgsConstructor
public class UpdateMusicUsecase {
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;
    private final CopyPropertiesStrategy copyPropertiesStrategy;
    private final CategoryDataProvider categoryDataProvider;

    @Transactional
    public MusicOutputDTO exec(Long musicianId, Music musicUpdate) {
        this.validateInputs(musicianId, musicUpdate);

        Musician musician = this.checkIfMusicianExists(musicianId);
        this.checkIfMusicianIsDisabled(musician);

        Music music = this.checkIfMusicExists(musicUpdate.getId());
        this.checkIfMusicIsDisabled(music);

        this.checkIfMusicBelongsToMusician(music, musician);

        if (Objects.nonNull(musicUpdate.getAlbumOrder())) {
            this.handleUpdatedMusicOrderOnAlbum(musicUpdate, music);
        }

        if (Objects.nonNull(musicUpdate.getIsSingle())) {
            if (!musicUpdate.getIsSingle()) {
                throw new ConflictException("To turn a Music into a non-single, use the functionality that insert an music into an album");
            } else {
                music.setAlbum(null);
                music.setAlbumOrder(null);
                music.setIsSingle(true);
            }
        }

        this.handleCategory(musicUpdate, music);

        copyPropertiesStrategy.copyNonNullProps(musicUpdate, music);

        Music updateMusic = this.musicDataProvider.register(music);

        return this.mountOutput(updateMusic);
    }

    private void validateInputs(Long musicianId, Music musicUpdate) {
        if (Objects.isNull(musicianId)) {
            throw new BadRequestException("Musician id can't be empty");
        }

        if (Objects.isNull(musicUpdate)) {
            throw new BadRequestException("Music updated can't be empty");
        }

        if (Objects.isNull(musicUpdate.getId())) {
            throw new BadRequestException("Music updated id can't be empty");
        }
    }

    private Musician checkIfMusicianExists(Long musicianId) {
        return this.musicianDataProvider.findById(musicianId).orElseThrow(() -> new NotFoundException("Musician"));
    }

    private void checkIfMusicianIsDisabled(Musician musician) {
        if (musician.getDisabled()) {
            throw new ForbiddenException("Musician is disabled");
        }
    }

    private Music checkIfMusicExists(UUID musicId) {
        return this.musicDataProvider.findById(musicId).orElseThrow(() -> new NotFoundException("Music"));
    }

    private void checkIfMusicIsDisabled(Music music) {
        if (music.getDisabled()) {
            throw new ForbiddenException("Music is disabled");
        }
    }

    private void checkIfMusicBelongsToMusician(Music music, Musician musician) {
        Long musicianId = musician.getId();
        Long musicMusicianId = music.getMusician().getId();

        if (!musicMusicianId.equals(musicianId)) {
            throw new ForbiddenException("Music don't belongs to musician provided");
        }
    }

    private void handleCategory(Music updatedMusic, Music currentMusic) {
        Category newCategory = updatedMusic.getCategory();
        Category currentCategory = currentMusic.getCategory();

        if (Objects.isNull(newCategory.getId())) {
            updatedMusic.setCategory(currentCategory);
        } else {
            Category getNewCategory = this.checkIfCategoryExists(newCategory.getId());

            updatedMusic.setCategory(getNewCategory);
        }
    }

    Category checkIfCategoryExists(UUID categoryId) {
        return this.categoryDataProvider.findById(categoryId).orElseThrow(() -> new NotFoundException("Category"));
    }

    private void handleUpdatedMusicOrderOnAlbum(Music updatedMusic, Music currentMusic) {
        Album musicAlbum = this.musicHasAlbum(currentMusic);

        if (Objects.isNull(musicAlbum)) {
            throw new ConflictException("Musics can have order album updated only when music belongs to an album");
        }

        Long albumMaxOrderPermitted = this.checkAlbumMusicLastOrder(musicAlbum);

        if (updatedMusic.getAlbumOrder() < 0) {
            throw new BadRequestException("Album order can't be less than 0");
        } else if (updatedMusic.getAlbumOrder() > albumMaxOrderPermitted) {
            throw new ConflictException("New order to music cant be more than last current album music max order");
        }

        this.updateAlbumOrderToFitUpdatedMusic(updatedMusic.getAlbumOrder(), currentMusic.getAlbumOrder(), musicAlbum.getId());
    }

    private Album musicHasAlbum(Music updatedMusic) {
        return Objects.nonNull(updatedMusic.getAlbum()) ? updatedMusic.getAlbum() : null;
    }

    private void updateAlbumOrderToFitUpdatedMusic(Long musicNewOrder, Long musicOldOrder, UUID albumId) {
        this.musicDataProvider.updateOrderToFitNewOneOnAlbum(musicNewOrder, musicOldOrder, albumId);
    }

    private Long checkAlbumMusicLastOrder(Album album) {
        return this.musicDataProvider.findLastMusicOnAlbumOrder(album.getMusician().getId(), album.getId());
    }

    private MusicOutputDTO mountOutput(Music music) {
        return MusicOutputDTO.builder()
            .id(music.getId())
            .name(music.getName())
            .category(CategoryOutputDTO.builder()
                .id(music.getCategory().getId())
                .name(music.getCategory().getName().name())
                .build()
            )
            .disabled(music.getDisabled())
            .order(Objects.nonNull(music.getAlbumOrder()) ? Math.toIntExact(music.getAlbumOrder()) : null)
            .createdAt(music.getCreatedAt())
            .duration(music.getDuration())
            .isSingle(music.getIsSingle())
            .build();
    }
}
