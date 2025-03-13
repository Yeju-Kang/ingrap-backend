package com.ingrap.backend.module.adminfurniture.service;

import com.ingrap.backend.module.adminfurniture.domain.AdminFurniture;
import com.ingrap.backend.module.adminfurniture.repository.AdminFurnitureRepository;
import com.ingrap.backend.module.common.domain.FurnitureType;
import com.ingrap.backend.module.common.domain.FurnitureStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminFurnitureService {

    private final AdminFurnitureRepository repository;
    private final String uploadDir = "uploads/admin/";

    public AdminFurniture registerFurniture(String name, FurnitureType type, MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String storedFileName = uuid + "_" + file.getOriginalFilename();
        String filePath = uploadDir + storedFileName;

        Files.createDirectories(Paths.get(uploadDir));
        file.transferTo(Paths.get(filePath));

        AdminFurniture furniture = AdminFurniture.builder()
                .name(name)
                .modelUrl(filePath)
                .fileSize(file.getSize())
                .type(type)
                .status(FurnitureStatus.NEW)
                .build();

        return repository.save(furniture);
    }
}
