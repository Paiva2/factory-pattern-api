package com.root.pattern.adapter.dto.playlist;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ListOwnPlaylistsOutputdTO {
    int page;
    int perPage;
    long totalPlaylists;
    List<PlaylistDetailsOutputDTO> playlists;
}
