package com.ingrap.backend.module.space.controller;

import com.ingrap.backend.module.space.dto.SpaceRequestDTO;
import com.ingrap.backend.module.space.dto.SpaceResponseDTO;
import com.ingrap.backend.module.space.service.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/space")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping
    public ResponseEntity<SpaceResponseDTO> createSpace(@RequestBody SpaceRequestDTO request) {
        SpaceResponseDTO response = spaceService.createSpace(request);
        return ResponseEntity.ok(response);
    }
}
