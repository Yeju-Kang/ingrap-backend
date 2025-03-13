package com.ingrap.backend.module.adminfurniture.repository;

import com.ingrap.backend.module.adminfurniture.domain.AdminFurniture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminFurnitureRepository extends JpaRepository<AdminFurniture, Long> {
}
