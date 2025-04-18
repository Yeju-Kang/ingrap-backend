package com.ingrap.backend.module.product.repository;

import com.ingrap.backend.module.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 특정 사용자가 등록한 상품 목록
     */
    List<Product> findBySellerId(Long sellerId);

    /**
     * 관리자 승인 대기 중인 상품 목록
     */
    List<Product> findByIsApprovedFalse();

    /**
     * 현재 노출 중인 상품 목록
     */
    List<Product> findByIsVisibleTrueAndIsApprovedTrue();
}
