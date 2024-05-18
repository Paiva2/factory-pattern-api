package com.root.pattern.adapter.controller.favourite;

import com.root.pattern.adapter.dto.favourite.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/favourite")
public interface FavouriteController {
    @PostMapping("/new/{musicId}")
    ResponseEntity<NewFavouriteOutputDTO> create(Authentication authentication, UUID musicId);

    @DeleteMapping("/{favouriteId}")
    ResponseEntity<DeleteFavouriteOutputDTO> remove(Authentication authentication, UUID favouriteId);

    @GetMapping("/list")
    ResponseEntity<ListOwnFavouritesDTO> list(Authentication authentication, int page, int perPage);

    @PatchMapping("/reorder/{favouriteId}")
    ResponseEntity<FavouriteOutputDTO> reorder(Authentication authentication, UUID favouriteId, ReoderFavouriteInputDTO dto);
}
