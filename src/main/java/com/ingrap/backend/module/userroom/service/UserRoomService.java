package com.ingrap.backend.module.userroom.service;

import com.ingrap.backend.module.common.domain.RoomStatus;
import com.ingrap.backend.module.userroom.domain.UserRoom;
import com.ingrap.backend.module.userroom.dto.UserRoomRequest;
import com.ingrap.backend.module.userroom.dto.UserRoomResponse;
import com.ingrap.backend.module.userroom.repository.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoomService {

    private final UserRoomRepository repository;

    public UserRoomResponse createRoom(UUID userId, UserRoomRequest request, String fileUrl) {
        UserRoom room = UserRoom.builder()
                .userId(userId)
                .name(request.getName())
                .description(request.getDescription())
                .fileUrl(fileUrl)
                .status(RoomStatus.UPLOAD)
                .build();

        UserRoom savedRoom = repository.save(room);
        return toResponse(savedRoom);
    }

    public List<UserRoomResponse> getRoomList(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserRoomResponse getRoomDetail(Long id) {
        UserRoom room = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        return toResponse(room);
    }

    public UserRoomResponse updateRoom(Long id, UserRoomRequest request) {
        UserRoom room = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        room.setName(request.getName());
        room.setDescription(request.getDescription());

        return toResponse(repository.save(room));
    }

    public void deleteRoom(Long id) {
        repository.deleteById(id);
    }

    private UserRoomResponse toResponse(UserRoom room) {
        return UserRoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .description(room.getDescription())
                .fileUrl(room.getFileUrl())
                .status(room.getStatus())
                .build();
    }
}
