package com.root.pattern.adapter.controller.album;

import com.root.pattern.adapter.dto.album.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/musician/album")
public interface AlbumController {
    @PostMapping("/new")
    ResponseEntity<NewAlbumOutputDTO> create(Authentication authentication, NewAlbumInputDTO input);

    @GetMapping("/list/{musicianId}")
    ResponseEntity<ListAllAlbumsOutputDTO> listAllMusicianAlbums(
        Long musicianId,
        Integer page,
        Integer perPage,
        String albumName
    );

    @GetMapping("/{albumId}")
    ResponseEntity<FilterAlbumOutputDTO> getMusicianAlbum(UUID albumId);

    @GetMapping("/all")
    ResponseEntity<ListAllAlbumsOutputDTO> getAllAlbumsByName(Integer page, Integer perPage, String name);

    @DeleteMapping("/{albumId}")
    ResponseEntity<DisableAlbumOutputDTO> disable(Authentication authentication, UUID albumId);
}
