package com.root.pattern.adapter.controller.music;

import com.root.pattern.adapter.dto.music.FilterMusicOutputDTO;
import com.root.pattern.adapter.dto.music.ListFilterMusicOutputDTO;
import com.root.pattern.adapter.dto.music.NewMusicDTO;
import com.root.pattern.adapter.dto.music.NewMusicOutputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
