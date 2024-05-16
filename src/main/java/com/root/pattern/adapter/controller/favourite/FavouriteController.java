package com.root.pattern.adapter.controller.favourite;

import com.root.pattern.adapter.dto.favourite.DeleteFavouriteOutputDTO;
import com.root.pattern.adapter.dto.favourite.NewFavouriteOutputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/favourite")
public interface FavouriteController {
    @PostMapping("/new/{musicId}")
    ResponseEntity<NewFavouriteOutputDTO> create(Authentication authentication, UUID musicId);

    @DeleteMapping("/{favouriteId}")
    ResponseEntity<DeleteFavouriteOutputDTO> remove(Authentication authentication, UUID favouriteId);
}
