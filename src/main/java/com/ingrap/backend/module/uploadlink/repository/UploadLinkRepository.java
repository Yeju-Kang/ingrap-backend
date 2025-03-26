package com.ingrap.backend.module.uploadlink.repository;

import com.ingrap.backend.module.uploadlink.entity.UploadLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UploadLinkRepository extends JpaRepository<UploadLink, UUID> {
    Optional<UploadLink> findByIdAndUsedFalse(UUID id);
}