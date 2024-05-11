package com.root.pattern.domain.usecase.album;

import com.root.pattern.adapter.dto.album.AlbumOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;
import java.util.UUID;

// TODO: TESTS
@Builder
@AllArgsConstructor
public class UpdateAlbumUsecase {
    private final MusicianDataProvider musicianDataProvider;
    private final AlbumDataProvider albumDataProvider;

    public AlbumOutputDTO exec(Long musicianId, Album album) {
        this.inputsValidation(musicianId, album);

        Musician musician = this.checkIfMusicianExists(musicianId);
        Album currentAlbum = this.checkIfAlbumExists(album.getId());

        this.checkIfMusicianIsDisabled(musician);
        this.checkIfAlbumIsDisabled(currentAlbum);

        this.checkIfAlbumBelongsToMusician(currentAlbum, musician);

        Album updatedCurrentAlbum = this.changeAlbumInformations(album, currentAlbum);

        Album performUpdate = this.albumDataProvider.update(updatedCurrentAlbum);

        return this.mountOutput(performUpdate);
    }

    public void inputsValidation(Long musicianId, Album album) {
        if (Objects.isNull(musicianId)) {
            throw new BadRequestException("Musician id can't be empty");
        }

        if (Objects.isNull(album)) {
            throw new BadRequestException("Album updated can't be empty");
        }

        if (Objects.isNull(album.getId())) {
            throw new BadRequestException("Album id can't be empty");
        }
    }

    public Musician checkIfMusicianExists(Long musicianId) {
        return this.musicianDataProvider.findById(musicianId).orElseThrow(() -> new NotFoundException("Musician"));
    }

    public Album checkIfAlbumExists(UUID albumId) {
        return this.albumDataProvider.findById(albumId).orElseThrow(() -> new NotFoundException("Album"));
    }

    public void checkIfMusicianIsDisabled(Musician musician) {
        if (musician.isDisabled()) {
            throw new ForbiddenException("Musician is disabled");
        }
    }

    public void checkIfAlbumIsDisabled(Album album) {
        if (album.isDisabled()) {
            throw new ForbiddenException("Album is disabled");
        }
    }

    public void checkIfAlbumBelongsToMusician(Album album, Musician musician) {
        Long musicianId = musician.getId();
        Long albumMusicianId = album.getMusician().getId();

        if (!albumMusicianId.equals(musicianId)) {
            throw new ForbiddenException("Album don't belong to provided musician");
        }
    }

    public Album changeAlbumInformations(Album updatedAlbum, Album currentAlbum) {
        currentAlbum.setName(updatedAlbum.getName());

        return currentAlbum;
    }

    public AlbumOutputDTO mountOutput(Album album) {
        return AlbumOutputDTO.builder()
            .id(album.getId())
            .name(album.getName())
            .createdAt(album.getCreatedAt())
            .totalMusics(album.getMusic().size())
            .build();
    }
}
