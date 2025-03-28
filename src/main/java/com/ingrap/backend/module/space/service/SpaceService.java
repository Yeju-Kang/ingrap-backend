package com.ingrap.backend.module.space.service;

import com.ingrap.backend.module.space.dto.SpaceRequestDTO;
import com.ingrap.backend.module.space.dto.SpaceResponseDTO;
import com.ingrap.backend.module.space.entity.Space;
import com.ingrap.backend.module.space.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepository;

    public SpaceResponseDTO createSpace(SpaceRequestDTO request) {
        Space space = new Space();
        space.setName(request.getName());

        Space saved = spaceRepository.save(space);
        return new SpaceResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getUuid(),
                saved.getSavedAt().toString()
        );
    }
}
