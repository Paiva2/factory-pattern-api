package com.root.pattern.domain.interfaces.usecase.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.domain.entity.Musician;

public interface UpdateMusicianProfileUsecase {
    MusicianOutputDTO exec(Musician musician);

    void validateInputs(Musician musician);

    void validateEmail(String email);

    void checkIfEmailAlreadyExists(String newEmail, Musician musician);

    Musician checkIfMusicianExists(Long id);

    MusicianOutputDTO mountOutput(Musician musician);

    String hashNewPassword(String password);
}
