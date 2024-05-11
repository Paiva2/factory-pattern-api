package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.PlaylistMusic;
import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class PlaylistMusicDataProviderimpl implements PlaylistMusicDataProvider {
    private final PlaylistMusicRepository playlistMusicRepository;

    @Override
    public void disableAllByMusicId(UUID musicId) {
        this.playlistMusicRepository.disableAllByMusic(musicId);
    }

    @Override
    public Long findLastMusicOrder(UUID playlistMusicId) {
        return this.playlistMusicRepository.findLastMusicOrder(playlistMusicId);
    }

    @Override
    public PlaylistMusic register(PlaylistMusic playlistMusic) {
        return this.playlistMusicRepository.save(playlistMusic);
    }

    @Override
    public Optional<PlaylistMusic> findByPlaylistAndMusic(UUID playlistId, UUID musicId) {
        return this.playlistMusicRepository.findByPlaylistAndMusic(playlistId, musicId);
    }
}
