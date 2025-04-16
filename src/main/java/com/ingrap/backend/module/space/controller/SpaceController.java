package com.ingrap.backend.module.space.controller;

import com.ingrap.backend.module.space.dto.SaveSpaceRequest;
import com.ingrap.backend.module.space.dto.SpaceDetailResponse;
import com.ingrap.backend.module.space.dto.SpaceResponse;
import com.ingrap.backend.module.space.service.SpaceService;
import com.ingrap.backend.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    /**
     * ✅ 비회원용 공간 생성
     */
    @PostMapping("/guest")
    public ResponseEntity<Long> createGuestSpace(@RequestBody Map<String, String> body) {
        Long id = spaceService.createGuestSpace(body.get("name"));
        return ResponseEntity.ok(id);
    }

    /**
     * ✅ 로그인 사용자용 공간 생성
     */
    @PostMapping
    public ResponseEntity<Long> createUserSpace(@AuthenticationPrincipal CustomUserDetails user,
                                                @RequestBody Map<String, String> body) {
        Long id = spaceService.createUserSpace(user.getId(), body.get("name"));
        return ResponseEntity.ok(id);
    }

    /**
     * ✅ 공간 저장 (수정 or 생성 포함)
     */
    @PostMapping("/save")
    public ResponseEntity<Void> saveSpace(@AuthenticationPrincipal CustomUserDetails user,
                                          @RequestBody SaveSpaceRequest dto) {
        spaceService.saveSpace(user.getId(), dto);
        return ResponseEntity.ok().build();
    }

    /**
     * ✅ 내 공간 목록 조회
     */
    @GetMapping("/mine")
    public ResponseEntity<List<SpaceResponse>> mySpaces(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(spaceService.getUserSpaces(user.getId()));
    }

    /**
     * ✅ 공간 상세 조회 (가구 포함)
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpaceDetailResponse> getSpace(@PathVariable Long id,
                                                        @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(spaceService.getSpaceDetail(id, user.getId()));
    }
}
