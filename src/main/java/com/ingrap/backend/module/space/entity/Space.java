package com.ingrap.backend.module.space.entity;

import com.ingrap.backend.module.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Space {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private User user; // nullable

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
