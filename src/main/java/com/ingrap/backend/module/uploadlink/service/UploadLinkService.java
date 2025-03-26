package com.ingrap.backend.module.uploadlink.service;

import com.ingrap.backend.module.uploadlink.entity.UploadLink;
import com.ingrap.backend.module.uploadlink.repository.UploadLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UploadLinkService {

    private final UploadLinkRepository uploadLinkRepository;

    public String generateUploadUrl(String spaceId) {
        UploadLink link = UploadLink.builder()
                .spaceId(spaceId)
                .createdAt(LocalDateTime.now())
                .used(false)
                .build();

        uploadLinkRepository.save(link);

        return "https://ingrap.com/upload/" + link.getId();
    }
}