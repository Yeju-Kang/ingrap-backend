package com.ingrap.backend.module.userfurniture.dto;

import com.ingrap.backend.module.common.domain.FurnitureType;
import com.ingrap.backend.module.common.domain.FurnitureStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFurnitureResponse {
    private Long id;
    private String name;
    private FurnitureType type;
    private FurnitureStatus status;
    private String fileUrl;
}
