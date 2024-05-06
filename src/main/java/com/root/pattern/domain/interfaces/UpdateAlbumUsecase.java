package com.root.pattern.domain.interfaces;

import com.root.pattern.adapter.dto.album.AlbumOutputDTO;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Musician;

import java.util.UUID;

public interface UpdateAlbumUsecase {
    AlbumOutputDTO exec(Long musicianId, Album album);

    void inputsValidation(Long musicianId, Album album);

    Musician checkIfMusicianExists(Long musicianId);

    Album checkIfAlbumExists(UUID albumId);

    void checkIfMusicianIsDisabled(Musician musician);

    void checkIfAlbumIsDisabled(Album album);

    void checkIfAlbumBelongsToMusician(Album album, Musician musician);

    Album changeAlbumInformations(Album updatedAlbum, Album currentAlbum);

    AlbumOutputDTO mountOutput(Album album);
}
