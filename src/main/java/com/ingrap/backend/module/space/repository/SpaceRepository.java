package com.ingrap.backend.module.space.repository;

import com.ingrap.backend.module.space.entity.Space;
import com.ingrap.backend.module.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    // 🔐 내 공간 목록 조회용
    List<Space> findAllByUserId(Long userId);

    // 🔐 상세 조회 시 존재 여부 및 유저 확인용 (선택 사항: Optional<Space> findByIdAndUserId(...) 도 가능)
}
