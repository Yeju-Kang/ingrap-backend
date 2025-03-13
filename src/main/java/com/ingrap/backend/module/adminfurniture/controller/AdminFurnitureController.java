package com.ingrap.backend.module.adminfurniture.controller;

import com.ingrap.backend.module.adminfurniture.domain.AdminFurniture;
import com.ingrap.backend.module.adminfurniture.service.AdminFurnitureService;
import com.ingrap.backend.module.common.domain.FurnitureType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/furnitures")
@RequiredArgsConstructor
public class AdminFurnitureController {

    private final AdminFurnitureService service;

    @PostMapping("/uploads")
    public ResponseEntity<AdminFurniture> uploadFurniture(@RequestParam("file") MultipartFile file,
                                                          @RequestParam("name") String name,
                                                          @RequestParam("type") FurnitureType type) {
        try {
            AdminFurniture furniture = service.registerFurniture(name, type, file);
            return ResponseEntity.ok(furniture);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

