package com.root.pattern.domain.usecase.album;

import com.root.pattern.adapter.dto.album.DisableAlbumOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import com.root.pattern.domain.interfaces.usecase.DisableAlbumUsecase;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

// TODO: TESTS
@Builder
@AllArgsConstructor
public class DisableAlbumUsecaseImpl implements DisableAlbumUsecase {
    private final MusicianDataProvider musicianDataProvider;
    private final AlbumDataProvider albumDataProvider;
    private final MusicDataProvider musicDataProvider;

    @Transactional
    @Override
    public DisableAlbumOutputDTO exec(Long musicianId, UUID albumId) {
        this.inputsValidation(musicianId, albumId);

        Musician musician = this.checkIfMusicianExists(musicianId);
        Album album = this.checkIfAlbumExists(albumId);

        this.checkIfMusicianIsNotDisabled(musician);
        this.checkIfAlbumIsNotDisabled(album);
        this.checkIfAlbumBelongsToMusician(album, musician);

        Album albumDisabled = this.disableAlbum(album);
        this.disableAllMusicsFromAlbum(album);

        return this.mountOutput(albumDisabled);
    }

    @Override
    public void inputsValidation(Long musicianId, UUID albumId) {
        if (Objects.isNull(musicianId)) {
            throw new BadRequestException("Musician id can't be empty");
        }

        if (Objects.isNull(albumId)) {
            throw new BadRequestException("Album id can't be empty");
        }
    }

    @Override
    public Musician checkIfMusicianExists(Long musicianId) {
        return this.musicianDataProvider.findById(musicianId).orElseThrow(() -> new NotFoundException("Musician"));
    }

    @Override
    public Album checkIfAlbumExists(UUID albumId) {
        return this.albumDataProvider.findById(albumId).orElseThrow(() -> new NotFoundException("Album"));
    }

    @Override
    public void checkIfMusicianIsNotDisabled(Musician musician) {
        if (musician.isDisabled()) {
            throw new ForbiddenException("Musician is disabled");
        }
    }

    @Override
    public void checkIfAlbumIsNotDisabled(Album album) {
        if (album.isDisabled()) {
            throw new ForbiddenException("Album is already disabled");
        }
    }

    @Override
    public void checkIfAlbumBelongsToMusician(Album album, Musician musician) {
        Long musicianId = musician.getId();
        Long albumMusicianId = album.getMusician().getId();

        if (!albumMusicianId.equals(musicianId)) {
            throw new ForbiddenException("Album don't belong to musician provided");
        }
    }

    @Override
    public Album disableAlbum(Album album) {
        album.setDisabled(true);
        album.setDisabledAt(new Date());

        Album albumDisabled = this.albumDataProvider.disable(album);

        return albumDisabled;
    }

    @Override
    public void disableAllMusicsFromAlbum(Album album) {
        List<UUID> musicsId = album.getMusic().stream().map(Music::getId).collect(Collectors.toList());

        this.musicDataProvider.disableAllWithId(musicsId);
    }

    @Override
    public DisableAlbumOutputDTO mountOutput(Album album) {
        return DisableAlbumOutputDTO.builder()
            .albumDisabledId(album.getId())
            .disabledAt(album.getDisabledAt())
            .build();
    }
}
