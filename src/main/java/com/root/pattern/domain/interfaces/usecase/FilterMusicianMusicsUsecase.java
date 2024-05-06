package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.music.ListFilterMusicOutputDTO;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;
import org.springframework.data.domain.Page;

public interface FilterMusicianMusicsUsecase {
    ListFilterMusicOutputDTO exec(Long musicianId, Integer page, Integer perPage);

    void validateInputs(Long musicianId);

    Musician checkIfMusicianExists(Long musicianId);

    void checkIfMusicianIsNotDisabled(Musician musician);

    Page<Music> getAllMusicsFromMusician(Long musicianId, Integer page, Integer perPage);

    ListFilterMusicOutputDTO mountOutput(Page<Music> musics);
}
