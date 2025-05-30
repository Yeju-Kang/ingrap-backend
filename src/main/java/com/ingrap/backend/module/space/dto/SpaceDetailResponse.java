package com.ingrap.backend.module.space.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceDetailResponse {
    private Long id;
    private String name;
    private List<SaveSpaceRequest.FurnitureDTO> furnitures;
}

