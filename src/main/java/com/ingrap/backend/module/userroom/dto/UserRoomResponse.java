package com.ingrap.backend.module.userroom.dto;

import com.ingrap.backend.module.common.domain.RoomStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoomResponse {
    private Long id;
    private String name;
    private String description;
    private String fileUrl;
    private RoomStatus status;
}
