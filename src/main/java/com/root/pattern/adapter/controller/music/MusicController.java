package com.root.pattern.adapter.controller.music;

import com.root.pattern.adapter.dto.music.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/music")
public interface MusicController {
    @PostMapping("/new/category/{categoryId}")
    ResponseEntity<NewMusicOutputDTO> register(
        UUID categoryId,
        Authentication authentication,
        NewMusicDTO newMusicDTO
    );

    @GetMapping
    ResponseEntity<ListFilterMusicOutputDTO> getMusicByName(String name, Integer page, Integer size);

    @GetMapping("/{musicId}")
    ResponseEntity<FilterMusicOutputDTO> getMusic(UUID id);

    @GetMapping("/all/{musicianId}")
    ResponseEntity<ListFilterMusicOutputDTO> getMusicianMusics(
        Long id,
        Integer page,
        Integer perPage
    );

    @GetMapping("/own/list")
    ResponseEntity<ListFilterMusicOutputDTO> getOwnMusicianMusics(
        Integer page,
        Integer perPage,
        String name,
        String album,
        Authentication authentication
    );

    @DeleteMapping("/{musicId}")
    ResponseEntity<MusicOutputDTO> disable(
        Authentication authentication,
        UUID musicId
    );

    @PostMapping("/{musicId}/insert/album/{albumId}")
    ResponseEntity<MusicOutputDTO> insertOnAlbum(
        Authentication authentication,
        UUID musicId,
        UUID albumId
    );

    @PatchMapping("/{musicId}/remove/album")
    ResponseEntity<MusicOutputDTO> removeFromAlbum(
        Authentication authentication,
        UUID musicId
    );

    @PatchMapping("/{musicId}")
    ResponseEntity<MusicOutputDTO> update(
        Authentication authentication,
        UUID musicId,
        UpdateMusicInputDTO dto
    );
}
