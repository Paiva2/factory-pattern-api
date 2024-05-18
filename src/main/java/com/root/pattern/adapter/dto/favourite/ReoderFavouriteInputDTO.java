package com.root.pattern.adapter.dto.favourite;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ReoderFavouriteInputDTO {
    @NotNull
    @Min(value = 0, message = "newOrder can't be less than 0")
    private Integer newOrder;
}
