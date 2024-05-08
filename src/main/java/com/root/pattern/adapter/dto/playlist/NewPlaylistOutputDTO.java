package com.root.pattern.adapter.dto.playlist;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewPlaylistOutputDTO {
    private UUID id;
    private String name;
    private String coverImage;
    private Date createdAt;
}
