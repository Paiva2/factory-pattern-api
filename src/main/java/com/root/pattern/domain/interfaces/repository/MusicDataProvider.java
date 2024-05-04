package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface MusicDataProvider {
    Optional<Music> findByAlbumAndName(UUID albumId, String musicName);

    Music register(Music music);

    Page<Music> findAllByNameLike(Pageable pageable, String name);
}
