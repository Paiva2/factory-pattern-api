package com.root.pattern.domain.usecase.music;

import com.root.pattern.adapter.dto.music.NewMusicOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.CategoryDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Builder
public class RegisterMusicUsecase {
    private final MusicianDataProvider musicianDataProvider;
    private final AlbumDataProvider albumDataProvider;
    private final CategoryDataProvider categoryDataProvider;
    private final MusicDataProvider musicDataProvider;

    public NewMusicOutputDTO exec(Long musicianId, Music music) {
        this.validateInputs(musicianId, music);
        this.validateIfMusicIsSingle(music);

        Musician musician = this.validateIfMusicianExists(musicianId);
        music.setMusician(musician);

        Category category = this.validateIfCategoryExists(music.getCategory().getId());
        music.setCategory(category);

        if (Objects.nonNull(music.getAlbum()) && Objects.nonNull(music.getAlbum().getId())) {
            Album album = this.validateIfAlbumExists(music.getAlbum().getId());

            if (Objects.nonNull(album.getDisabledAt())) {
                throw new ForbiddenException("Album disabled");
            }

            this.validateIfMusicianHasMusicWithSameNameOnAlbum(album.getId(), music.getName());

            Long lastOrderedMusicOnAlbum = this.checkLastMusicOrderOnAlbum(musician.getId(), album.getId());
            Long musicOrder = Objects.nonNull(lastOrderedMusicOnAlbum) ? lastOrderedMusicOnAlbum + 1 : 0L;

            music.setAlbum(album);
            music.setAlbumOrder(musicOrder);
        }

        Music saveMusic = this.musicDataProvider.register(music);

        return this.mountOutput(saveMusic);
    }

    public void validateInputs(Long musicianId, Music music) {
        if (Objects.isNull(musicianId)) {
            throw new BadRequestException("Musician id can't be empty");
        }

        if (Objects.isNull(music)) {
            throw new BadRequestException("Music can't be empty");
        }
    }

    public Musician validateIfMusicianExists(Long musicianId) {
        return this.musicianDataProvider.findById(musicianId).orElseThrow(() -> new NotFoundException("Musician"));
    }

    public Album validateIfAlbumExists(UUID albumId) {
        return this.albumDataProvider.findById(albumId).orElseThrow(() -> new NotFoundException("Album"));
    }

    public void validateIfMusicIsSingle(Music music) {
        if (music.isSingle() && Objects.nonNull(music.getAlbum()) && Objects.nonNull(music.getAlbum().getId())) {
            throw new BadRequestException("Music can't have an album if it's a single");
        } else if (!music.isSingle() && Objects.isNull(music.getAlbum())) {
            throw new BadRequestException("Music must have an album if it's not a single");
        }
    }

    public Category validateIfCategoryExists(UUID categoryId) {
        return this.categoryDataProvider.findById(categoryId).orElseThrow(() -> new NotFoundException("Category"));
    }

    public void validateIfMusicianHasMusicWithSameNameOnAlbum(UUID albumId, String musicName) {
        Music music = this.musicDataProvider.findByAlbumAndName(albumId, musicName).orElse(null);

        if (Objects.nonNull(music)) {
            throw new ConflictException("Album already has an music with this title");
        }
    }

    public Long checkLastMusicOrderOnAlbum(Long musicianId, UUID albumId) {
        return this.musicDataProvider.findLastMusicOnAlbumOrder(musicianId, albumId);
    }

    public NewMusicOutputDTO mountOutput(Music music) {
        return NewMusicOutputDTO.builder()
            .musicId(music.getId())
            .name(music.getName())
            .duration(music.getDuration())
            .albumName(Objects.nonNull(music.getAlbum()) ? music.getAlbum().getName() : null)
            .categoryName(music.getCategory().getName().toString())
            .isSingle(music.isSingle())
            .createdAt(music.getCreatedAt())
            .build();
    }
}
