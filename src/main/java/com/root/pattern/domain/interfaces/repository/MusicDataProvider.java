package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MusicDataProvider {
    Optional<Music> findByAlbumAndName(UUID albumId, String musicName);

    Music register(Music music);

    Page<Music> findAllByNameLike(Pageable pageable, String name);

    Optional<Music> findById(UUID id);

    void disableAllWithId(List<UUID> ids);

    Page<Music> findAllByMusician(Pageable pageable, Long musicianId);

    List<Music> findAllFromMusician(Long musicianId, Integer perPage, Integer offSet, String name, String albumName);

    Long findAllFromMusicianCount(Long musicianId, String name, String albumName);

    Long findLastMusicOnAlbumOrder(Long musicianId, UUID albumId);

    Optional<Music> findByAlbum(UUID albumId, UUID musicId);

    void decreaseAllOrderFromMusicOnAlbum(UUID albumId, Integer decreaseValue, Long musicAlbumOrderRemoved);
}
