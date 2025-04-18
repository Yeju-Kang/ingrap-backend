package com.ingrap.backend.module.product.entity;

import com.ingrap.backend.module.common.domain.FurnitureType;
import com.ingrap.backend.module.common.domain.FurnitureStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품명
    private String name;

    // 3D 모델 및 썸네일
    private String modelUrl;
    private String imageUrl;
    private long fileSize;

    // 타입, 상태
    @Enumerated(EnumType.STRING)
    private FurnitureType type;

    @Enumerated(EnumType.STRING)
    private FurnitureStatus status;

    // 카테고리
    private String mainCategory;
    private String subCategory;

    // 사이즈 정보
    private int width;
    private int depth;
    private int height;

    // 색상 정보
    @ElementCollection
    private List<String> colors;

    // 가격 정보
    private int price;
    private float discountRate;
    private int finalPrice;

    // 상세 설명 및 비고
    @Lob
    private String description;

    @Column(length = 1000)
    private String note;

    // 사용자 확장용 필드
    private Long sellerId;          // null이면 관리자 등록
    private Boolean isApproved;     // 관리자 승인 여부
    private Boolean isVisible;      // 사이트 노출 여부

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (isApproved == null) this.isApproved = false;
        if (isVisible == null) this.isVisible = false;
    }
}
