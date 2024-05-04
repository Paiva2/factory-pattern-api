package com.root.pattern.adapter.dto.album;

import com.root.pattern.adapter.dto.music.MusicOutputDTO;
import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FilterAlbumOutputDTO {
    private UUID id;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private MusicianOutputDTO musician;
    private List<MusicOutputDTO> musics;
}
