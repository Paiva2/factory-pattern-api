package com.root.pattern.adapter.controller.album;

import com.root.pattern.adapter.dto.album.*;
import com.root.pattern.domain.interfaces.usecase.album.CreateAlbumUsecase;
import com.root.pattern.domain.interfaces.usecase.album.UpdateAlbumUsecase;
import com.root.pattern.domain.interfaces.usecase.album.DisableAlbumUsecase;
import com.root.pattern.domain.interfaces.usecase.album.FilterAlbumUsecase;
import com.root.pattern.domain.interfaces.usecase.album.FilterAlbumsNameUsecase;
import com.root.pattern.domain.interfaces.usecase.musician.ListMusicianAlbumsUsecase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class AlbumControllerImpl implements AlbumController {
    private final CreateAlbumUsecase createAlbumUsecase;
    private final ListMusicianAlbumsUsecase listMusicianAlbumsUsecase;
    private final FilterAlbumUsecase filterAlbumUsecase;
    private final FilterAlbumsNameUsecase filterAlbumsNameUsecase;
    private final DisableAlbumUsecase disableAlbumUsecase;
    private final UpdateAlbumUsecase updateAlbumUsecase;

    @Override
    public ResponseEntity<NewAlbumOutputDTO> create(
        Authentication authentication,
        @RequestBody @Valid NewAlbumInputDTO input
    ) {
        NewAlbumOutputDTO output = this.createAlbumUsecase.exec(Long.valueOf(authentication.getName()), input.toEntity());

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @Override
    public ResponseEntity<ListAllAlbumsOutputDTO> listAllMusicianAlbums(
        @PathVariable(value = "musicianId") Long musicianId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "size", defaultValue = "5") Integer perPage,
        @RequestParam(value = "name", required = false) String albumName
    ) {
        ListAllAlbumsOutputDTO output = this.listMusicianAlbumsUsecase.exec(musicianId, page, perPage, albumName);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<FilterAlbumOutputDTO> getMusicianAlbum(
        @PathVariable("albumId") UUID albumId
    ) {
        FilterAlbumOutputDTO output = this.filterAlbumUsecase.exec(albumId);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<ListAllAlbumsOutputDTO> getAllAlbumsByName(
        @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
        @RequestParam(value = "size", defaultValue = "5", required = false) Integer perPage,
        @RequestParam(value = "name") String name
    ) {
        ListAllAlbumsOutputDTO output = this.filterAlbumsNameUsecase.exec(name, page, perPage);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<DisableAlbumOutputDTO> disable(
        Authentication authentication,
        @PathVariable("albumId") UUID albumId
    ) {
        Long musicianId = Long.valueOf(authentication.getName());

        DisableAlbumOutputDTO output = this.disableAlbumUsecase.exec(musicianId, albumId);

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }

    @Override
    public ResponseEntity<AlbumOutputDTO> update(
        Authentication authentication,
        @PathVariable("albumId") UUID albumId,
        @RequestBody @Valid UpdateAlbumInputDTO dto
    ) {
        Long musicianId = Long.valueOf(authentication.getName());

        AlbumOutputDTO output = this.updateAlbumUsecase.exec(musicianId, dto.toEntity(albumId));

        return ResponseEntity.status(HttpStatus.OK).body(output);
    }
}
