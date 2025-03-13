package com.ingrap.backend.module.userroom.controller;

import com.ingrap.backend.module.userroom.dto.UserRoomRequest;
import com.ingrap.backend.module.userroom.dto.UserRoomResponse;
import com.ingrap.backend.module.userroom.service.UserRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-room")
@RequiredArgsConstructor
public class UserRoomController {

    private final UserRoomService service;

    @PostMapping
    public ResponseEntity<UserRoomResponse> createRoom(
            @RequestParam UUID userId,
            @RequestPart UserRoomRequest request,
            @RequestPart MultipartFile file) {

        // 파일 업로드 로직 추가 필요
        String fileUrl = "/uploads/" + file.getOriginalFilename(); // 임시로 지정
        UserRoomResponse response = service.createRoom(userId, request, fileUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserRoomResponse>> getRoomList(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getRoomList(userId));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<UserRoomResponse> getRoomDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.getRoomDetail(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRoomResponse> updateRoom(@PathVariable Long id, @RequestBody UserRoomRequest request) {
        return ResponseEntity.ok(service.updateRoom(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        service.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
