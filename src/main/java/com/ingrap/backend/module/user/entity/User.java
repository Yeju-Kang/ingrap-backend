package com.ingrap.backend.module.user.entity;

import com.ingrap.backend.module.user.domain.UserType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @PrePersist
    protected void onCreate() {
        if (this.userType == null) {
            this.userType = UserType.INDIVIDUAL;
        }
    }
}
