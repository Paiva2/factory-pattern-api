package com.root.pattern.adapter.controller.album;

import com.root.pattern.adapter.dto.album.ListAllAlbumsOutputDTO;
import com.root.pattern.adapter.dto.album.NewAlbumInputDTO;
import com.root.pattern.adapter.dto.album.NewAlbumOutputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
