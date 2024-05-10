package com.root.pattern.adapter.dto.playlist;

import com.root.pattern.adapter.dto.playlistMusics.PlaylistMusicOutputDTO;
import com.root.pattern.adapter.dto.user.UserOutputDTO;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetPlaylistOutputDTO {
    private UUID id;
    private String name;
    private String coverImage;
    private Integer order;
    private UserOutputDTO user;
    private Date createdAt;
    private Date updatedAt;
    private List<PlaylistMusicOutputDTO> musics;
}
