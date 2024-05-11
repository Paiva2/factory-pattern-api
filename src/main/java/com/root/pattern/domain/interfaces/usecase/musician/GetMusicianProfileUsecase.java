package com.root.pattern.domain.interfaces.usecase.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.domain.entity.Musician;

public interface GetMusicianProfileUsecase {
    MusicianOutputDTO exec(Long id);

    void validateInput(Long id);

    Musician checkIfMusicianExists(Long id);

    void checkIfMusicianIsDisabled(Musician musician);

    MusicianOutputDTO mountOutput(Musician musician);
}
