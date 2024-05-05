package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.music.ListFilterMusicOutputDTO;
import com.root.pattern.domain.entity.Music;
import org.springframework.data.domain.Page;

public interface FilterMusicsNameUsecase {
    ListFilterMusicOutputDTO exec(String musicName, int page, int perPage);

    void validateInputs(String musicName);

    Page<Music> getAllMusicsWithName(String name, int page, int perPage);

    ListFilterMusicOutputDTO mountOutput(Page<Music> musicList, int perPage);
}
