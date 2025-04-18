package com.ingrap.backend.module.product.mapper;

import com.ingrap.backend.module.product.dto.ProductRequest;
import com.ingrap.backend.module.product.dto.ProductResponse;
import com.ingrap.backend.module.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest dto) {
        return Product.builder()
                .name(dto.getName())
                .modelUrl(dto.getModelUrl())
                .imageUrl(dto.getImageUrl())
                .fileSize(dto.getFileSize())
                .type(dto.getType())
                .mainCategory(dto.getMainCategory())
                .subCategory(dto.getSubCategory())
                .width(dto.getWidth())
                .depth(dto.getDepth())
                .height(dto.getHeight())
                .colors(dto.getColors())
                .price(dto.getPrice())
                .discountRate(dto.getDiscountRate())
                .finalPrice(dto.getFinalPrice())
                .description(dto.getDescription())
                .note(dto.getNote())
                .isVisible(dto.getIsVisible())
                .build();
    }

    public ProductResponse toResponse(Product entity) {
        return ProductResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .modelUrl(entity.getModelUrl())
                .imageUrl(entity.getImageUrl())
                .fileSize(entity.getFileSize())
                .type(entity.getType())
                .status(entity.getStatus())
                .mainCategory(entity.getMainCategory())
                .subCategory(entity.getSubCategory())
                .width(entity.getWidth())
                .depth(entity.getDepth())
                .height(entity.getHeight())
                .colors(entity.getColors())
                .price(entity.getPrice())
                .discountRate(entity.getDiscountRate())
                .finalPrice(entity.getFinalPrice())
                .description(entity.getDescription())
                .note(entity.getNote())
                .sellerId(entity.getSellerId())
                .isApproved(entity.getIsApproved())
                .isVisible(entity.getIsVisible())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
