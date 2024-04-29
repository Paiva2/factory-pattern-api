package com.root.pattern.adapter.controller.musician;

import com.root.pattern.adapter.dto.musician.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/musician")
public interface MusicianController {
    @PostMapping("/register")
    ResponseEntity<MusicianOutputDTO> register(RegisterMusicianDTO dto);

    @GetMapping("/profile")
    ResponseEntity<MusicianOutputDTO> profile(Authentication authentication);

    @PostMapping("/login")
    ResponseEntity<AuthMusicianDTO> login(LoginMusicianDTO dto);

    @PatchMapping("/profile")
    ResponseEntity<MusicianOutputDTO> updateProfile(Authentication authentication, UpdateMusicianProfileDTO dto);
}
