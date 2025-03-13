package com.ingrap.backend.module.userfurniture.domain;

import com.ingrap.backend.module.common.domain.FurnitureType;
import com.ingrap.backend.module.common.domain.FurnitureStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_furniture")
public class UserFurniture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId; // 사용자 ID

    private String name; // 가구 이름

    @Enumerated(EnumType.STRING)
    private FurnitureType type; // 가구 종류 (ENUM)

    @Enumerated(EnumType.STRING)
    private FurnitureStatus status; // 가구 상태 (ENUM)

    private String fileUrl; // 업로드된 파일 경로 (이미지/3D 파일 등)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}