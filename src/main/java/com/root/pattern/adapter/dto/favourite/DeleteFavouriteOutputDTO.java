package com.root.pattern.adapter.dto.favourite;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DeleteFavouriteOutputDTO {
    private UUID id;
}
