package com.root.pattern.adapter.dto.album;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class DisableAlbumOutputDTO {
    private UUID albumDisabledId;
    private Date disabledAt;
}
