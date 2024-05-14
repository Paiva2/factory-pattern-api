package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.PlaylistMusic;

import java.util.Optional;
import java.util.UUID;

public interface PlaylistMusicDataProvider {
    void disableAllByMusicId(UUID musicId);

    Long findLastMusicOrder(UUID playlistId);

    PlaylistMusic register(PlaylistMusic playlistMusic);

    Optional<PlaylistMusic> findByPlaylistAndMusic(UUID playlistId, UUID musicId);

    Optional<PlaylistMusic> findById(UUID id);

    void decreaseAllOrderFromMusicOnPlaylist(UUID playlistId, Integer positionBeingRemoved);

    void increaseAllOrderFromMusicOnPlaylist(UUID playlistId, Integer positionFrom, Integer oldPosition);

    void delete(UUID playlistMusicId);
}
