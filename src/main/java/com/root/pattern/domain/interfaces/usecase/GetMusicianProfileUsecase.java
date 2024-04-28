package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.domain.entity.Musician;

public interface GetMusicianProfileUsecase {
    MusicianOutputDTO exec(Long id);

    void validateInput(Long id);

    Musician checkIfMusicianExists(Long id);

    MusicianOutputDTO mountOutput(Musician musician);
}
