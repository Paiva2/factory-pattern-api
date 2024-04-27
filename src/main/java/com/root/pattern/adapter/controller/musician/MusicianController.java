package com.root.pattern.adapter.controller.musician;

import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.dto.musician.RegisterMusicianDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/musician")
public interface MusicianController {
    @PostMapping("/register")
    ResponseEntity<MusicianOutputDTO> register(RegisterMusicianDTO dto);
}
