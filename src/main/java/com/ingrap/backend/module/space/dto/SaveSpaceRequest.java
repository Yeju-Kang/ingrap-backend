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
public class SaveSpaceRequest {
    private Long spaceId; // 기존 공간 수정 시 사용 (신규는 null)
    private String name;
    private List<FurnitureDTO> furnitures;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FurnitureDTO {
        private String type;
        private String modelUrl;
        private float positionX;
        private float positionY;
        private float positionZ;
        private float rotationX;
        private float rotationY;
        private float rotationZ;
        private String color;
    }
}
