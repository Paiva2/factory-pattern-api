package com.root.pattern.domain.interfaces.usecase;

import com.root.pattern.adapter.dto.music.NewMusicOutputDTO;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Category;
import com.root.pattern.domain.entity.Music;
import com.root.pattern.domain.entity.Musician;

import java.util.UUID;

public interface RegisterMusicUsecase {
    NewMusicOutputDTO exec(Long musicianId, Music music);

    void validateInputs(Long musicianId, Music music);

    Musician validateIfMusicianExists(Long musicianId);

    Album validateIfAlbumExists(UUID albumId);

    void validateIfMusicIsSingle(Music music);

    Category validateIfCategoryExists(UUID categoryId);

    void validateIfMusicianHasMusicWithSameNameOnAlbum(UUID albumId, String musicName);

    Long checkLastMusicOrderOnAlbum(Long musicianId, UUID albumId);

    NewMusicOutputDTO mountOutput(Music music);
}
