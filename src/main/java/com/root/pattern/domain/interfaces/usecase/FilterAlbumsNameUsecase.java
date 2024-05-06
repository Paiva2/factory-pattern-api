package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.album.ListAllAlbumsOutputDTO;
import com.root.pattern.domain.entity.Album;
import org.springframework.data.domain.Page;

public interface FilterAlbumsNameUsecase {
    ListAllAlbumsOutputDTO exec(String name, int page, int perPage);

    void inputValidations(String name);

    Page<Album> getAllAlbums(String name, int page, int perPage);

    ListAllAlbumsOutputDTO mountOutput(Page<Album> albums);
}
