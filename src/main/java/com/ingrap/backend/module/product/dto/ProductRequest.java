package com.ingrap.backend.module.product.dto;

import com.ingrap.backend.module.common.domain.FurnitureType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    private String name;
    private String modelUrl;
    private String imageUrl;
    private long fileSize;

    private FurnitureType type;
    private String mainCategory;
    private String subCategory;

    private int width;
    private int depth;
    private int height;

    private List<String> colors;

    private int price;
    private float discountRate;
    private int finalPrice;

    private String description;
    private String note;

    // 판매자 등록 확장용
    private Boolean isVisible;     // 유저가 최초 등록 시 false로 설정 가능
}
