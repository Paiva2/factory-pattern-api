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

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

//TODO: TESTS
@Builder
@AllArgsConstructor
public class InsertMusicOnAlbumUsecase {
    private final AlbumDataProvider albumDataProvider;
    private final MusicianDataProvider musicianDataProvider;
    private final MusicDataProvider musicDataProvider;

    public MusicOutputDTO exec(Long musicianId, UUID albumId, UUID musicId) {
        this.validateInputs(musicianId, albumId, musicId);

        Musician musician = this.checkIfMusicianExists(musicianId);
        this.checkIfMusicianIsDisabled(musician);

        Album album = this.checkIfAlbumExists(albumId);
        this.checkIfAlbumIsDisabled(album);

        Music music = this.checkIfMusicExists(musicId);
        this.checkIfMusicIsDisabled(music);

        Music musicInserted = this.insertMusicAlbum(music, album);

        return this.mountOutput(musicInserted);
    }

    public void validateInputs(Long musicianId, UUID albumId, UUID musicId) {
        if (Objects.isNull(musicianId)) {
            throw new BadRequestException("Musician id can't be empty");
        }

        if (Objects.isNull(albumId)) {
            throw new BadRequestException("Album id can't be empty");
        }

        if (Objects.isNull(musicId)) {
            throw new BadRequestException("Music id can't be empty");
        }
    }

    public Musician checkIfMusicianExists(Long musicianId) {
        return this.musicianDataProvider.findById(musicianId).orElseThrow(() -> new NotFoundException("Musician"));
    }

    public void checkIfMusicianIsDisabled(Musician musician) {
        if (musician.isDisabled()) {
            throw new ForbiddenException("Musician is disabled");
        }
    }

    public Album checkIfAlbumExists(UUID albumId) {
        return this.albumDataProvider.findById(albumId).orElseThrow(() -> new NotFoundException("Album"));
    }

    public void checkIfAlbumIsDisabled(Album album) {
        if (album.isDisabled()) {
            throw new ForbiddenException("Album is disabled");
        }
    }

    public void checkIfAlbumBelongsToMusician(Album album, Musician musician) {
        Long musicianId = musician.getId();
        Long albumMusicianId = album.getMusician().getId();

        if (!musicianId.equals(albumMusicianId)) {
            throw new ForbiddenException("Album do not belong to musician");
        }
    }

    public Music checkIfMusicExists(UUID musicId) {
        return this.musicDataProvider.findById(musicId).orElseThrow(() -> new NotFoundException("Music"));
    }

    public void checkIfMusicIsDisabled(Music music) {
        if (music.isDisabled()) {
            throw new ForbiddenException("Music is disabled");
        }
    }

    public void checkIfMusicAlreadyHasAlbum(Music music) {
        if (Objects.nonNull(music.getAlbum())) {
            throw new ConflictException("Music already has an album");
        }
    }

    public void checkIfAlbumAlreadyHasMusic(Music music, Album album) {
        Optional<Music> musicOnAlbum = this.musicDataProvider.findByAlbum(album.getId(), music.getId());

        if (musicOnAlbum.isPresent()) {
            throw new ConflictException("Album already has this music");
        }
    }

    public Long getAlbumLastMusicOrder(Long musicianId, UUID albumId) {
        return this.musicDataProvider.findLastMusicOnAlbumOrder(musicianId, albumId);
    }

    public Music insertMusicAlbum(Music music, Album album) {
        this.checkIfMusicAlreadyHasAlbum(music);
        this.checkIfAlbumAlreadyHasMusic(music, album);

        Long lastOrderOnAlbum = this.getAlbumLastMusicOrder(music.getMusician().getId(), album.getId());
        Long newMusicOrder = Objects.isNull(lastOrderOnAlbum) ? 0 : lastOrderOnAlbum + 1;

        music.setSingle(false);
        music.setAlbum(album);
        music.setAlbumOrder(newMusicOrder);

        return this.musicDataProvider.register(music);
    }

    public MusicOutputDTO mountOutput(Music music) {
        return MusicOutputDTO.builder()
            .id(music.getId())
            .name(music.getName())
            .disabled(music.isDisabled())
            .duration(music.getDuration())
            .order(Objects.nonNull(music.getAlbumOrder()) ? Math.toIntExact(music.getAlbumOrder()) : null)
            .createdAt(music.getCreatedAt())
            .category(CategoryOutputDTO.builder()
                .id(music.getCategory().getId())
                .name(music.getCategory().getName().name())
                .build()
            )
            .isSingle(music.isSingle())
            .build();
    }
}
