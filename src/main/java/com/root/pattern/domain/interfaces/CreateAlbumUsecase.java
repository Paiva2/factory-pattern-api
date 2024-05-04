package com.root.pattern.domain.interfaces;

import com.root.pattern.adapter.dto.album.NewAlbumOutputDTO;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Musician;

public interface CreateAlbumUsecase {
    NewAlbumOutputDTO exec(Long musicianId, Album newAlbum);

    void inputValidations(Long musicianId, Album newAlbum);

    Musician checkIfMusicianExists(Long musicianId);

    void checkIfMusicianHasAlbum(String albumName, Long musicianId);

    NewAlbumOutputDTO mountOutput(Album album);
}
