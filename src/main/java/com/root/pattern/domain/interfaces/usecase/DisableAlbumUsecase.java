package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.album.DisableAlbumOutputDTO;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Musician;

import java.util.UUID;

public interface DisableAlbumUsecase {
    DisableAlbumOutputDTO exec(Long musicianId, UUID albumId);

    void inputsValidation(Long musicianId, UUID albumId);

    Musician checkIfMusicianExists(Long musicianId);

    Album checkIfAlbumExists(UUID albumId);

    void checkIfMusicianIsNotDisabled(Musician musician);

    void checkIfAlbumIsNotDisabled(Album album);

    void checkIfAlbumBelongsToMusician(Album album, Musician musician);

    Album disableAlbum(Album album);

    void disableAllMusicsFromAlbum(Album album);

    DisableAlbumOutputDTO mountOutput(Album album);
}
