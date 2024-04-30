package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.Music;

import java.util.Optional;
import java.util.UUID;

public interface MusicDataProvider {
    Optional<Music> findByAlbumAndName(UUID albumId, String musicName);

    Music register(Music music);
}
