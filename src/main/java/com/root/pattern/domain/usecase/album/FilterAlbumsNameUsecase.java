package com.root.pattern.domain.usecase.album;

import com.root.pattern.adapter.dto.album.AlbumOutputDTO;
import com.root.pattern.adapter.dto.album.ListAllAlbumsOutputDTO;
import com.root.pattern.adapter.exceptions.BadRequestException;
import com.root.pattern.domain.entity.Album;
import com.root.pattern.domain.interfaces.repository.AlbumDataProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;
import java.util.stream.Collectors;

// todo: tests
@AllArgsConstructor
@Builder
public class FilterAlbumsNameUsecase {
    private final AlbumDataProvider albumDataProvider;

    public ListAllAlbumsOutputDTO exec(String name, int page, int perPage) {
        this.inputValidations(name);

        if (page < 1) {
            page = 1;
        }

        if (perPage < 5) {
            perPage = 5;
        } else if (perPage > 100) {
            perPage = 100;
        }

        Page<Album> albums = this.getAllAlbums(name, page, perPage);

        return this.mountOutput(albums);
    }

    public void inputValidations(String name) {
        if (Objects.isNull(name)) {
            throw new BadRequestException("Album name can't be empty");
        }
    }

    public Page<Album> getAllAlbums(String name, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.Direction.DESC, "createdAt");

        return this.albumDataProvider.findAllByNameLike(pageable, name);
    }

    public ListAllAlbumsOutputDTO mountOutput(Page<Album> albums) {
        return ListAllAlbumsOutputDTO.builder()
            .page(albums.getNumber() + 1)
            .perPage(albums.getSize())
            .totalAlbuns(albums.getTotalElements())
            .albuns(albums.getContent().stream().map(album -> AlbumOutputDTO.builder()
                .id(album.getId())
                .name(album.getName())
                .totalMusics(album.getMusic().size())
                .createdAt(album.getCreatedAt())
                .build()
            ).collect(Collectors.toList()))
            .build();
    }
}
