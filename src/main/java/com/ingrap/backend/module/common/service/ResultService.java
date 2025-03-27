package com.ingrap.backend.module.common.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ResultService {

    private final String baseResultDir = "results";

    public Resource loadResultFile(String category, String fileId) throws IOException {
        Path filePath = Paths.get(baseResultDir, category, fileId).normalize();

        if (!Files.exists(filePath)) {
            throw new NoSuchFileException("파일이 존재하지 않습니다.");
        }

        return new FileSystemResource(filePath);
    }
}
