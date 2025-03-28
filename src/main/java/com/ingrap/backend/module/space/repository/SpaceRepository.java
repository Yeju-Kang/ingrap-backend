package com.ingrap.backend.module.space.repository;

import com.ingrap.backend.module.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceRepository extends JpaRepository<Space, Long> {
}
