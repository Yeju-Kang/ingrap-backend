package com.ingrap.backend.module.space.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "space")
@Getter @Setter
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime savedAt = LocalDateTime.now();

    @Column(unique = true, nullable = false, updatable = false)
    private String uuid = UUID.randomUUID().toString();
}