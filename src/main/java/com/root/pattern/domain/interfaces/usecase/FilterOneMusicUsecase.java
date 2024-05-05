package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.music.FilterMusicOutputDTO;
import com.root.pattern.domain.entity.Music;

import java.util.UUID;

public interface FilterOneMusicUsecase {
    FilterMusicOutputDTO exec(UUID id);

    void validateInputs(UUID id);

    Music checkIfMusicExists(UUID id);

    FilterMusicOutputDTO mountOutput(Music music);
}
