package com.ingrap.backend.module.space.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpaceResponseDTO {
    private Long id;
    private String name;
    private String uuid;
    private String savedAt;
}
