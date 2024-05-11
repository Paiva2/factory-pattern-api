package com.root.pattern.domain.interfaces.usecase.album;

import com.root.pattern.adapter.dto.album.FilterAlbumOutputDTO;
import com.root.pattern.domain.entity.Album;

import java.util.UUID;

public interface FilterAlbumUsecase {
    FilterAlbumOutputDTO exec(UUID albumId);

    void validateInputs(UUID albumId);

    Album checkIfAlbumExists(UUID albumId);

    FilterAlbumOutputDTO mountOutput(Album album);
}
