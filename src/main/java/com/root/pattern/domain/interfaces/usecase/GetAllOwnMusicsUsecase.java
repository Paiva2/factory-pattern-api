package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.music.ListFilterMusicOutputDTO;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;

import java.util.List;

public interface GetAllOwnMusicsUsecase {
    ListFilterMusicOutputDTO exec(Long musicianId, Integer page, Integer perPage, String name, String albumName);

    void validateInputs(Long musicianId);

    Musician checkIfMusicianExists(Long musicianId);

    void checkIfMusicianIsDisabled(Musician musician);

    List<Music> getAllMusics(Long musicianId, Integer page, Integer perPage, String name, String albumName);

    Long getAllMusicsCount(Long musicianId, String name, String albumName);

    ListFilterMusicOutputDTO mountOutput(List<Music> musics, Integer page, Integer perPage, Long totalItems);
}
