package com.root.pattern.adapter.controller.musician;

import com.root.pattern.adapter.dto.musician.AuthMusicianDTO;
import com.root.pattern.adapter.dto.musician.LoginMusicianDTO;
import com.root.pattern.adapter.dto.musician.MusicianOutputDTO;
import com.root.pattern.adapter.dto.musician.RegisterMusicianDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/musician")
public interface MusicianController {
    @PostMapping("/register")
    ResponseEntity<MusicianOutputDTO> register(RegisterMusicianDTO dto);

    @GetMapping("/profile")
    ResponseEntity<MusicianOutputDTO> profile(Authentication authentication);

    @PostMapping("/login")
    ResponseEntity<AuthMusicianDTO> login(LoginMusicianDTO dto);
}
