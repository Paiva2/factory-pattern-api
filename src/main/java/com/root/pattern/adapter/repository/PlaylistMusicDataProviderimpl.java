package com.root.pattern.adapter.repository;

import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class PlaylistMusicDataProviderimpl implements PlaylistMusicDataProvider {
    private final PlaylistMusicRepository playlistMusicRepository;

    @Override
    public void disableAllByMusicId(UUID musicId) {
        this.playlistMusicRepository.disableAllByMusic(musicId);
    }
}
