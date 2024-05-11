package com.root.pattern.domain.usecase.album;

import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.music.MusicOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ConflictException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;

//TODO: TESTS
@AllArgsConstructor
@Builder
public class RemoveMusicFromAlbumUsecase {
    private final AlbumDataProvider albumDataProvider;
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;

    @Transactional
    public MusicOutputDTO exec(Long musicianId, UUID musicId) {
        this.validateInputs(musicianId, musicId);

        Musician musician = this.checkIfMusicianExists(musicianId);
        this.checkIfMusicianIsDisabled(musician);

        Music music = this.checkIfMusicExists(musicId);
        this.checkIfMusicIsDisabled(music);

        this.checkIfMusicHasAlbum(music);

        Album album = music.getAlbum();

        this.decreaseAllOrderFromMusicOnAlbum(album, music.getAlbumOrder());

        Music musicWithoutAlbum = this.removeAlbumFromMusic(music);

        this.checkIfMusicBelongsToMusician(musician, music);

        Music persistMusic = this.musicDataProvider.register(musicWithoutAlbum);

        return this.mountOutput(persistMusic);
    }

    private void validateInputs(Long musicianId, UUID musicId) {
        if (Objects.isNull(musicianId)) {
            throw new BadRequestException("Musician id can't be empty");
        }

        if (Objects.isNull(musicId)) {
            throw new BadRequestException("Music id can't be empty");
        }
    }

    private Musician checkIfMusicianExists(Long musicianId) {
        return this.musicianDataProvider.findById(musicianId).orElseThrow(() -> new NotFoundException("Musician"));
    }

    private void checkIfMusicianIsDisabled(Musician musician) {
        if (musician.isDisabled()) {
            throw new ForbiddenException("Musician is disabled");
        }
    }

    private Music checkIfMusicExists(UUID musicId) {
        return this.musicDataProvider.findById(musicId).orElseThrow(() -> new NotFoundException("Music"));
    }

    private void checkIfMusicIsDisabled(Music music) {
        if (music.isDisabled()) {
            throw new ForbiddenException("Music is disabled");
        }
    }

    private void checkIfMusicHasAlbum(Music music) {
        if (Objects.isNull(music.getAlbum())) {
            throw new ConflictException("Music has no album");
        }
    }

    private void decreaseAllOrderFromMusicOnAlbum(Album album, Long musicAlbumOrderRemoved) {
        Integer decreaseValue = 1;

        this.musicDataProvider.decreaseAllOrderFromMusicOnAlbum(album.getId(), decreaseValue, musicAlbumOrderRemoved);
    }

    private Music removeAlbumFromMusic(Music music) {
        music.setAlbum(null);
        music.setSingle(true);
        music.setAlbumOrder(null);

        return music;
    }

    private void checkIfMusicBelongsToMusician(Musician musician, Music music) {
        Long musicianId = musician.getId();
        Long musicMusicianId = music.getMusician().getId();

        if (!musicMusicianId.equals(musicianId)) {
            throw new ForbiddenException("Music do not belong to provided musician");
        }
    }

    private MusicOutputDTO mountOutput(Music music) {
        return MusicOutputDTO.builder()
            .id(music.getId())
            .name(music.getName())
            .duration(music.getDuration())
            .order(Objects.isNull(music.getAlbumOrder()) ? null : Math.toIntExact(music.getAlbumOrder()))
            .isSingle(music.isSingle())
            .createdAt(music.getCreatedAt())
            .disabled(music.isDisabled())
            .category(CategoryOutputDTO.builder()
                .id(music.getId())
                .name(music.getName())
                .build()
            )
            .build();
    }
}
