package com.root.pattern.adapter.dto.musician;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthMusicianDTO {
    String email;
    String authToken;
}



