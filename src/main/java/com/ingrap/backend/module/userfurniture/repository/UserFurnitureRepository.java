package com.ingrap.backend.module.userfurniture.repository;

import com.ingrap.backend.module.userfurniture.domain.UserFurniture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserFurnitureRepository extends JpaRepository<UserFurniture, Long> {
    List<UserFurniture> findByUserId(UUID userId);
}
