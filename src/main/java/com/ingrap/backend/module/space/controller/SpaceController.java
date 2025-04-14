package com.ingrap.backend.module.space.controller;

import com.ingrap.backend.module.space.dto.SpaceRequestDTO;
import com.ingrap.backend.module.space.service.SpaceService;
import com.ingrap.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping("/guest")
    public ResponseEntity<Long> createGuestSpace(@RequestBody Map<String, String> body) {
        Long id = spaceService.createGuestSpace(body.get("name"));
        return ResponseEntity.ok(id);
    }

    @PostMapping("/save")
    public ResponseEntity<Void> saveSpace(@AuthenticationPrincipal CustomUserDetails user,
                                          @RequestBody SaveSpaceRequest dto) {
        spaceService.saveSpace(user.getId(), dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<SpaceResponse>> mySpaces(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(spaceService.getUserSpaces(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceDetailResponse> getSpace(@PathVariable Long id,
                                                        @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(spaceService.getSpaceDetail(id, user.getId()));
    }
}

