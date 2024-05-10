package com.root.pattern.adapter.dto.playlistMusics;

import com.root.pattern.adapter.dto.music.MusicOutputDTO;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class PlaylistMusicOutputDTO {
    private UUID id;
    private Date createdAt;
    private MusicOutputDTO music;
}
