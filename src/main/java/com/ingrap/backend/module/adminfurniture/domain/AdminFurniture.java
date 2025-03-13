package com.ingrap.backend.module.adminfurniture.domain;

import com.ingrap.backend.module.common.domain.FurnitureType;
import com.ingrap.backend.module.common.domain.FurnitureStatus;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "admin_furnitures")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminFurniture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String modelUrl;
    private long fileSize;

    @Enumerated(EnumType.STRING)
    private FurnitureType type;

    @Enumerated(EnumType.STRING)
    private FurnitureStatus status;
}