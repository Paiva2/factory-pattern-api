package com.root.pattern.adapter.dto.album;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ListAllAlbumsOutputDTO {
    int page;
    int perPage;
    long totalAlbuns;
    List<AlbumOutputDTO> albuns;
}
