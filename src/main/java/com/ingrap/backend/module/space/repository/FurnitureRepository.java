package com.ingrap.backend.module.space.repository;

import com.ingrap.backend.module.space.entity.Furniture;
import com.ingrap.backend.module.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FurnitureRepository extends JpaRepository<Furniture, Long> {

    // 🔐 공간 저장 시 기존 가구 삭제
    void deleteBySpace(Space space);

    // 🔐 공간 상세 조회 시 가구 목록 조회
    List<Furniture> findAllBySpace(Space space);
}
