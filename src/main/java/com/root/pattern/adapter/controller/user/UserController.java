package com.root.pattern.adapter.controller.user;

import com.root.pattern.adapter.dto.user.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public interface UserController {
    @PostMapping("/register")
    ResponseEntity<UserOutputDTO> registerNewUser(RegisterUserDTO dto);

    @PostMapping("/login")
    ResponseEntity<AuthUserDTO> authenticateUser(LoginUserDTO dto);

    @GetMapping("/profile")
    ResponseEntity<UserOutputDTO> getProfile(Authentication authentication);

    @PatchMapping("/profile")
    ResponseEntity<UserOutputDTO> updateProfile(Authentication authentication, UpdateUserProfileDTO dto);
}
