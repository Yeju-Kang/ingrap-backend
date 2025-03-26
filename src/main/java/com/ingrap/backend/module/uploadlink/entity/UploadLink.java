package com.ingrap.backend.module.uploadlink.entity;

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
public class UploadLink {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String spaceId;

    private LocalDateTime createdAt;

    private boolean used;
}