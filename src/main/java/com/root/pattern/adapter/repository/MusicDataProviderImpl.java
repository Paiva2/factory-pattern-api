package com.root.pattern.adapter.repository;

import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.interfaces.repository.MusicDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Component
public class MusicDataProviderImpl implements MusicDataProvider {
    private final MusicRepository musicRepository;

    @Override
    public Optional<Music> findByAlbumAndName(UUID albumId, String musicName) {
        return this.musicRepository.findByAlbumAndName(albumId, musicName);
    }

    @Override
    public Music register(Music music) {
        return this.musicRepository.save(music);
    }
}
