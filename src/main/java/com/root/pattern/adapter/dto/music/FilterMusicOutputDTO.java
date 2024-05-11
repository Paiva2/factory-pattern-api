package com.root.pattern.adapter.dto.music;

import com.root.pattern.adapter.dto.album.AlbumOutputDTO;
import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FilterMusicOutputDTO {
    private UUID id;
    private Long duration;
    private String name;
    private Boolean isSingle;
    private CategoryOutputDTO category;
    private Date createdAt;
    private MusicianOutputDTO musician;
    private AlbumOutputDTO album;
}
