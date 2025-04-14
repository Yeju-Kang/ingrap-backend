package com.ingrap.backend.module.space.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Furniture {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Space space;

    private String type;
    private String modelUrl;
    private float positionX, positionY, positionZ;
    private float rotationX, rotationY, rotationZ;
    private String color;
}
