package com.root.pattern.domain.interfaces.usecase.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.domain.entity.Musician;

public interface AuthMusicianUsecase {
    MusicianOutputDTO exec(Musician musician);

    void validateInputs(Musician musician);

    Musician checkIfMusicianExists(String email);

    void checkIfPasswordsMatches(String raw, String hashed);

    MusicianOutputDTO mountOutput(Musician musician);
}
