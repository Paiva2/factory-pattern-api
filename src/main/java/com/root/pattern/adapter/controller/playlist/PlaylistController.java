package com.root.pattern.adapter.controller.playlist;

import com.root.pattern.adapter.dto.playlist.GetPlaylistOutputDTO;
import com.root.pattern.adapter.dto.playlist.ListOwnPlaylistsOutputDTO;
import com.root.pattern.adapter.dto.playlist.NewPlaylistInputDTO;
import com.root.pattern.adapter.dto.playlist.NewPlaylistOutputDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
