package com.root.pattern.domain.interfaces.usecase.musician;

import com.root.pattern.adapter.dto.musician.FilterMusicianOutputDTO;
import com.root.pattern.domain.entity.Musician;

public interface FilterMusicianUsecase {
    FilterMusicianOutputDTO exec(Long musicianId, String musicianName);

    void validateInputs(Long musicianId, String musicianName);

    Musician checkIfMusicianExists(Long musicianId, String musicianName);

    Musician findMusicianId(Long musicianId);

    Musician findMusicianName(String name);

    void checkIfMusicianIsDisabled(Musician musician);

    FilterMusicianOutputDTO mountOutput(Musician musician);
}
