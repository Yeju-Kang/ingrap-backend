package com.ingrap.backend.module.product.dto;

import com.ingrap.backend.module.common.domain.FurnitureType;
import com.ingrap.backend.module.common.domain.FurnitureStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String modelUrl;
    private String imageUrl;
    private long fileSize;

    private FurnitureType type;
    private FurnitureStatus status;

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

    private Long sellerId;        // 사용자 등록 상품 여부 확인용
    private Boolean isApproved;   // 승인 여부
    private Boolean isVisible;    // 노출 여부

    private LocalDateTime createdAt;
}
