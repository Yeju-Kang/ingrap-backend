package com.ingrap.backend.module.space.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceResponse {
    private Long id;
    private String name;
    private String thumbnailUrl;
    private String createdAt;
}
