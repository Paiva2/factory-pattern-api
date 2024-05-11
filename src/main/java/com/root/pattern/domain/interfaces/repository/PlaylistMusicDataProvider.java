package com.root.pattern.domain.interfaces.repository;

import com.root.pattern.domain.entity.PlaylistMusic;

import java.util.Optional;
import java.util.UUID;

public interface PlaylistMusicDataProvider {
    void disableAllByMusicId(UUID musicId);

    Long findLastMusicOrder(UUID playlistMusic);

    PlaylistMusic register(PlaylistMusic playlistMusic);

    Optional<PlaylistMusic> findByPlaylistAndMusic(UUID playlistId, UUID musicId);
}
