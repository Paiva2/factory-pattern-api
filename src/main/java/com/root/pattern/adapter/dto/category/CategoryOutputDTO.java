package com.root.pattern.adapter.dto.category;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CategoryOutputDTO {
    private UUID id;
    private String name;
}
