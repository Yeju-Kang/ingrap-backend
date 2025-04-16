package com.ingrap.backend.module.space.service;

import com.ingrap.backend.module.space.entity.Furniture;
import com.ingrap.backend.module.space.entity.Space;
import com.ingrap.backend.module.space.dto.SaveSpaceRequest;
import com.ingrap.backend.module.space.dto.SpaceDetailResponse;
import com.ingrap.backend.module.space.dto.SpaceResponse;
import com.ingrap.backend.module.space.repository.FurnitureRepository;
import com.ingrap.backend.module.space.repository.SpaceRepository;
import com.ingrap.backend.module.user.entity.User;
import com.ingrap.backend.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final FurnitureRepository furnitureRepository;
    private final UserRepository userRepository;

    /**
     * 🔓 비회원 게스트 공간 생성
     */
    @Transactional
    public Long createGuestSpace(String name) {
        Space space = Space.builder()
                .name(name)
                .user(null)
                .build();
        return spaceRepository.save(space).getId();
    }

    /**
     * 🔐 로그인 사용자 공간 생성
     */
    @Transactional
    public Long createUserSpace(Long userId, String name) {
        User user = getUserOrThrow(userId);

        Space space = Space.builder()
                .name(name)
                .user(user)
                .build();

        return spaceRepository.save(space).getId();
    }

    /**
     * 🔐 공간 저장 (가구 포함) + 소유권 이전
     */
    @Transactional
    public void saveSpace(Long userId, SaveSpaceRequest dto) {
        Space space;

        if (dto.getSpaceId() != null) {
            space = spaceRepository.findById(dto.getSpaceId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 공간이 존재하지 않습니다."));

            if (space.getUser() == null) {
                space.setUser(getUserOrThrow(userId)); // ✅ 소유권 이전
            } else if (!space.getUser().getId().equals(userId)) {
                throw new SecurityException("해당 공간에 접근할 수 없습니다.");
            }

            space.setName(dto.getName());
            furnitureRepository.deleteBySpace(space);

        } else {
            User user = getUserOrThrow(userId);
            space = Space.builder()
                    .name(dto.getName())
                    .user(user)
                    .build();
            spaceRepository.save(space);
        }

        List<Furniture> furnitureList = dto.getFurnitures().stream()
                .map(f -> Furniture.builder()
                        .space(space)
                        .type(f.getType())
                        .modelUrl(f.getModelUrl())
                        .positionX(f.getPositionX())
                        .positionY(f.getPositionY())
                        .positionZ(f.getPositionZ())
                        .rotationX(f.getRotationX())
                        .rotationY(f.getRotationY())
                        .rotationZ(f.getRotationZ())
                        .color(f.getColor())
                        .build())
                .collect(Collectors.toList());

        furnitureRepository.saveAll(furnitureList);
    }

    /**
     * 🔐 내 공간 목록 조회
     */
    @Transactional(readOnly = true)
    public List<SpaceResponse> getUserSpaces(Long userId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return spaceRepository.findAllByUserId(userId).stream()
                .map(s -> SpaceResponse.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .thumbnailUrl(null)
                        .createdAt(s.getCreatedAt().format(formatter))
                        .updatedAt(s.getUpdatedAt() != null ? s.getUpdatedAt().format(formatter) : s.getCreatedAt().format(formatter))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 🔐 공간 상세 조회 (가구 포함)
     */
    @Transactional(readOnly = true)
    public SpaceDetailResponse getSpaceDetail(Long spaceId, Long userId) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공간이 없습니다."));

        if (space.getUser() == null || !space.getUser().getId().equals(userId)) {
            throw new SecurityException("해당 공간에 접근할 수 없습니다.");
        }

        List<SaveSpaceRequest.FurnitureDTO> furnitureList = furnitureRepository.findAllBySpace(space).stream()
                .map(f -> SaveSpaceRequest.FurnitureDTO.builder()
                        .type(f.getType())
                        .modelUrl(f.getModelUrl())
                        .positionX(f.getPositionX())
                        .positionY(f.getPositionY())
                        .positionZ(f.getPositionZ())
                        .rotationX(f.getRotationX())
                        .rotationY(f.getRotationY())
                        .rotationZ(f.getRotationZ())
                        .color(f.getColor())
                        .build())
                .collect(Collectors.toList());

        return SpaceDetailResponse.builder()
                .id(space.getId())
                .name(space.getName())
                .furnitures(furnitureList)
                .build();
    }

    /**
     * 🔐 유저 조회 공통 메서드
     */
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
    }
}