package com.ingrap.backend.module.product.service;

import com.ingrap.backend.module.common.domain.FurnitureStatus;
import com.ingrap.backend.module.product.dto.ProductRequest;
import com.ingrap.backend.module.product.dto.ProductResponse;
import com.ingrap.backend.module.product.entity.Product;
import com.ingrap.backend.module.product.mapper.ProductMapper;
import com.ingrap.backend.module.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    /**
     * 상품 등록
     */
    public ProductResponse create(ProductRequest request) {
        Product product = mapper.toEntity(request);
        product.setStatus(FurnitureStatus.NEW);
        return mapper.toResponse(repository.save(product));
    }

    /**
     * 전체 상품 목록
     */
    public List<ProductResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 단건 조회
     */
    public ProductResponse findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
        return mapper.toResponse(product);
    }

    /**
     * 상품 수정
     */
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        // 수정 항목 반영
        product.setName(request.getName());
        product.setModelUrl(request.getModelUrl());
        product.setImageUrl(request.getImageUrl());
        product.setFileSize(request.getFileSize());
        product.setType(request.getType());
        product.setMainCategory(request.getMainCategory());
        product.setSubCategory(request.getSubCategory());
        product.setWidth(request.getWidth());
        product.setDepth(request.getDepth());
        product.setHeight(request.getHeight());
        product.setColors(request.getColors());
        product.setPrice(request.getPrice());
        product.setDiscountRate(request.getDiscountRate());
        product.setFinalPrice(request.getFinalPrice());
        product.setDescription(request.getDescription());
        product.setNote(request.getNote());
        product.setIsVisible(request.getIsVisible());

        return mapper.toResponse(repository.save(product));
    }

    /**
     * 상품 삭제
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
