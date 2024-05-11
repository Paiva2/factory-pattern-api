package com.root.pattern.domain.interfaces.usecase.musician;

import com.root.pattern.adapter.dto.album.ListAllAlbumsOutputDTO;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Musician;
import org.springframework.data.domain.Page;

public interface ListMusicianAlbumsUsecase {
    ListAllAlbumsOutputDTO exec(Long musicianId, Integer page, Integer perPage, String albumName);

    void validateInputs(Long musicianId);

    Musician validateIfMusicianExists(Long musicianId);

    ListAllAlbumsOutputDTO mountOutput(Page<Album> items, int perPage);
}
