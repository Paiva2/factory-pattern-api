package com.root.pattern.domain.interfaces.repository;

import java.util.UUID;

public interface PlaylistMusicDataProvider {
    void disableAllByMusicId(UUID musicId);
}
