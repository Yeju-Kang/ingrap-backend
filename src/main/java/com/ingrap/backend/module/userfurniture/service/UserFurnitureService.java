package com.ingrap.backend.module.userfurniture.service;

import com.ingrap.backend.module.userfurniture.domain.UserFurniture;
import com.ingrap.backend.module.userfurniture.dto.UserFurnitureRequest;
import com.ingrap.backend.module.userfurniture.dto.UserFurnitureResponse;
import com.ingrap.backend.module.userfurniture.repository.UserFurnitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFurnitureService {

    private final UserFurnitureRepository repository;

    public UserFurnitureResponse createFurniture(UUID userId, UserFurnitureRequest request, String fileUrl) {
        UserFurniture furniture = UserFurniture.builder()
                .userId(userId)
                .name(request.getName())
                .type(request.getType())
                .status(request.getStatus())
                .fileUrl(fileUrl)
                .build();

        UserFurniture savedFurniture = repository.save(furniture);
        return toResponse(savedFurniture);
    }

    public List<UserFurnitureResponse> getFurnitureList(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserFurnitureResponse getFurnitureDetail(Long id) {
        UserFurniture furniture = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Furniture not found"));
        return toResponse(furniture);
    }

    public UserFurnitureResponse updateFurniture(Long id, UserFurnitureRequest request) {
        UserFurniture furniture = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Furniture not found"));

        furniture.setName(request.getName());
        furniture.setType(request.getType());
        furniture.setStatus(request.getStatus());

        return toResponse(repository.save(furniture));
    }

    public void deleteFurniture(Long id) {
        repository.deleteById(id);
    }

    private UserFurnitureResponse toResponse(UserFurniture furniture) {
        return UserFurnitureResponse.builder()
                .id(furniture.getId())
                .name(furniture.getName())
                .type(furniture.getType())
                .status(furniture.getStatus())
                .fileUrl(furniture.getFileUrl())
                .build();
    }
}
