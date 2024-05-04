package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.Album;

import java.util.Optional;
import java.util.UUID;

public interface AlbumDataProvider {
    Optional<Album> findById(UUID id);

    Album register(Album album);

    Optional<Album> findByAlbumNameAndMusicianId(String albumName, Long musicianId);
}
