package com.root.pattern.domain.usecase.album;

import com.root.pattern.adapter.dto.album.AlbumOutputDTO;
import com.root.pattern.adapter.dto.album.ListAllAlbumsOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.adapter.exceptions.ForbiddenException;
import com.root.pattern.adapter.exceptions.NotFoundException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.entity.Musician;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import com.root.pattern.domain.interfaces.repository.MusicianDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
public class ListMusicianAlbumsUsecase {
    private final MusicianDataProvider musicianDataProvider;
    private final AlbumDataProvider albumDataProvider;

    public ListAllAlbumsOutputDTO exec(Long musicianId, Integer page, Integer perPage, String albumName) {
        this.validateInputs(musicianId);

        if (page < 1) {
            page = 1;
        }

        if (perPage < 5) {
            perPage = 5;
        } else if (perPage > 50) {
            perPage = 50;
        }

        Musician musician = this.validateIfMusicianExists(musicianId);

        if (musician.getDisabled()) {
            throw new ForbiddenException("Musician disabled");
        }

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.Direction.DESC, "createdAt");

        Page<Album> albumsList;

        if (Objects.isNull(albumName)) {
            albumsList = this.albumDataProvider.findAllByMusicianId(pageable, musician.getId());
        } else {
            albumsList = this.albumDataProvider.findAllByMusicianIdAndAlbumName(pageable, musician.getId(), albumName);
        }

        return this.mountOutput(albumsList, perPage);
    }

    public void validateInputs(Long musicianId) {
        if (Objects.isNull(musicianId)) {
            throw new BadRequestException("Musician id can't be null");
        }
    }

    public Musician validateIfMusicianExists(Long musicianId) {
        return this.musicianDataProvider.findById(musicianId).orElseThrow(() -> new NotFoundException("Musician"));
    }

    public ListAllAlbumsOutputDTO mountOutput(Page<Album> items, int perPage) {
        return ListAllAlbumsOutputDTO.builder()
            .page(items.getNumber() + 1)
            .perPage(perPage)
            .totalAlbuns(items.getTotalElements())
            .albuns(items.stream().map(album -> AlbumOutputDTO.builder()
                .id(album.getId())
                .name(album.getName())
                .createdAt(album.getCreatedAt())
                .totalMusics(album.getMusic().size())
                .build()).collect(Collectors.toList())
            ).build();
    }
}
