package com.ingrap.backend.module.uploadlink.controller;

import com.ingrap.backend.module.uploadlink.service.UploadLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/uploadlink")
@RequiredArgsConstructor
public class UploadLinkController {

    private final UploadLinkService uploadLinkService;

    @PostMapping
    public ResponseEntity<Map<String, String>> generateUploadLink(@RequestBody Map<String, String> request) {
        String spaceId = request.get("spaceId");
        String uploadUrl = uploadLinkService.generateUploadUrl(spaceId);

        return ResponseEntity.ok(Map.of("uploadUrl", uploadUrl));
    }
}
