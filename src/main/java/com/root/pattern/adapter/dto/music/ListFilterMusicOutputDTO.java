package com.root.pattern.adapter.dto.music;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ListFilterMusicOutputDTO {
    int page;
    int perPage;
    long totalItems;
    List<FilterMusicOutputDTO> musics;
}
