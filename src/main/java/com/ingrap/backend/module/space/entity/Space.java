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

    @Column(nullable = false, unique = true, updatable = false)
    private String uuid;

    private String name;

    @ManyToOne
    private User user; // nullable

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void generateFields() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
