package com.ingrap.backend.module.userroom.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoomRequest {
    private String name;
    private String description;
}
