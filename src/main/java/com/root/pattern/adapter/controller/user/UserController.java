package com.root.pattern.adapter.controller.user;

import com.root.pattern.adapter.dto.user.AuthUserDTO;
import com.root.pattern.adapter.dto.user.AuthenticateUserDTO;
import com.root.pattern.adapter.dto.user.RegisterUserDTO;
import com.root.pattern.adapter.dto.user.UserOutputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public interface UserController {

    @PostMapping("/register")
    ResponseEntity<UserOutputDTO> registerNewUser(RegisterUserDTO dto);

    @PostMapping("/login")
    ResponseEntity<AuthUserDTO> authenticateUser(AuthenticateUserDTO dto);
}
