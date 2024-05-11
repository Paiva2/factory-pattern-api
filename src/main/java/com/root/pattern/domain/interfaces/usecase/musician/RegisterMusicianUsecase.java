package com.root.pattern.domain.interfaces.usecase.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.domain.entity.Musician;

public interface RegisterMusicianUsecase {
    MusicianOutputDTO exec(Musician newMusician);

    void inputValidations(Musician newMusician);

    void checkIfUserAlreadyExists(String email, String name);

    String hashPassword(String rawPassword);

    MusicianOutputDTO mountDto(Musician newMusician);
}
