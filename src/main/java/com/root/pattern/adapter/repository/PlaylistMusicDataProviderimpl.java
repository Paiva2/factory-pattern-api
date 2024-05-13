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
    public Long findLastMusicOrder(UUID playlistId) {
        return this.playlistMusicRepository.findLastMusicOrder(playlistId);
    }

    @Override
    public PlaylistMusic register(PlaylistMusic playlistMusic) {
        return this.playlistMusicRepository.save(playlistMusic);
    }

    @Override
    public Optional<PlaylistMusic> findByPlaylistAndMusic(UUID playlistId, UUID musicId) {
        return this.playlistMusicRepository.findByPlaylistAndMusic(playlistId, musicId);
    }

    @Override
    public Optional<PlaylistMusic> findById(UUID id) {
        return this.playlistMusicRepository.findById(id);
    }

    @Override
    public void decreaseAllOrderFromMusicOnPlaylist(UUID playlistId, Integer positionBeingRemoved) {
        this.playlistMusicRepository.decreaseAllOrderFromMusicOnPlaylist(playlistId, positionBeingRemoved);
    }

    @Override
    public void delete(UUID playlistMusicId) {
        this.playlistMusicRepository.deleteById(playlistMusicId);
    }
}
