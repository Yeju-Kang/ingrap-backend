package com.ingrap.backend.module.userfurniture.controller;

import com.ingrap.backend.module.userfurniture.dto.UserFurnitureRequest;
import com.ingrap.backend.module.userfurniture.dto.UserFurnitureResponse;
import com.ingrap.backend.module.userfurniture.service.UserFurnitureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-furniture")
@RequiredArgsConstructor
public class UserFurnitureController {

    private final UserFurnitureService service;

    @PostMapping
    public ResponseEntity<UserFurnitureResponse> createFurniture(
            @RequestParam UUID userId,
            @RequestPart UserFurnitureRequest request,
            @RequestPart MultipartFile file) {

        // 파일 업로드 로직 추가 필요 (파일 서버에 저장 후 URL 생성)
        String fileUrl = "/uploads/" + file.getOriginalFilename(); // 임시로 지정
        UserFurnitureResponse response = service.createFurniture(userId, request, fileUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserFurnitureResponse>> getFurnitureList(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getFurnitureList(userId));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<UserFurnitureResponse> getFurnitureDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFurnitureDetail(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserFurnitureResponse> updateFurniture(@PathVariable Long id, @RequestBody UserFurnitureRequest request) {
        return ResponseEntity.ok(service.updateFurniture(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFurniture(@PathVariable Long id) {
        service.deleteFurniture(id);
        return ResponseEntity.noContent().build();
    }
}
