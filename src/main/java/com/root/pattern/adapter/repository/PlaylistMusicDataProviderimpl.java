package com.root.pattern.adapter.repository;

import com.root.pattern.domain.interfaces.repository.PlaylistMusicDataProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlaylistMusicDataProviderimpl implements PlaylistMusicDataProvider {
    private final PlaylistMusicRepository playlistMusicRepository;
}
