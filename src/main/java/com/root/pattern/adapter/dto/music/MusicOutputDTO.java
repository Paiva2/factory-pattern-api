package com.root.pattern.adapter.dto.music;

import com.root.pattern.adapter.dto.category.CategoryOutputDTO;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MusicOutputDTO {
    private UUID id;
    private Long duration;
    private String name;
    private CategoryOutputDTO category;
    private boolean isSingle;
    private Integer order;
    private boolean disabled;
    private Date createdAt;
}
