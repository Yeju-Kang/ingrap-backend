package com.ingrap.backend.module.product.controller;

import com.ingrap.backend.module.product.dto.ProductRequest;
import com.ingrap.backend.module.product.dto.ProductResponse;
import com.ingrap.backend.module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    /**
     * 상품 등록
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    /**
     * 상품 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * 상품 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
