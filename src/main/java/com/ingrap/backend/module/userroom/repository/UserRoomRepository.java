package com.ingrap.backend.module.userroom.repository;

import com.ingrap.backend.module.userroom.domain.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
    List<UserRoom> findByUserId(UUID userId);
}
