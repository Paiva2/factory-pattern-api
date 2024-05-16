package com.root.pattern.adapter.controller.playlist;

import com.root.pattern.adapter.dto.playlist.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/playlist")
public interface PlaylistController {
    @PostMapping("/new")
    ResponseEntity<NewPlaylistOutputDTO> create(
        Authentication authentication,
        NewPlaylistInputDTO dto
    );

    @GetMapping("/own/list")
    ResponseEntity<ListOwnPlaylistsOutputDTO> listOwn(
        Authentication authentication,
        Integer page,
        Integer perPage,
        String name
    );

    @GetMapping("/{playlistId}")
    ResponseEntity<GetPlaylistOutputDTO> get(UUID playlistId);

    @PostMapping("/{playlistId}/music/{musicId}")
    ResponseEntity<Void> newMusic(Authentication authentication, UUID playlistId, UUID musicId);

    @GetMapping("/export/{playlistId}")
    ResponseEntity<ByteArrayResource> export(UUID playlistId);

    @DeleteMapping("/music/{playlistMusicId}")
    ResponseEntity<GetPlaylistOutputDTO> deletePlaylistMusic(Authentication authentication, UUID playlistMusicId);

    @PatchMapping("/{playlistMusicId}/order/{newOrder}")
    ResponseEntity<GetPlaylistOutputDTO> reOrder(Authentication authentication, UUID playlistMusicId, Integer newOrder);

    @PostMapping("/copy/{playlistId}")
    ResponseEntity<GetPlaylistOutputDTO> copyOthers(Authentication authentication, UUID playlistId, CopyPlaylistInputDTO dto);
}
