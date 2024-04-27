package com.root.pattern.adapter.dto.musician;

import com.root.pattern.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class MusicianOutputDTO {
    private Long id;
    private String name;
    private String email;
    private Date createdAt;
    private Role role;
}
