package com.root.pattern.adapter.dto.music;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NewMusicOutputDTO {
    private UUID musicId;
    private Long duration;
    private String name;
    private String albumName;
    private String categoryName;
    private boolean isSingle;
    private Date createdAt;
}
