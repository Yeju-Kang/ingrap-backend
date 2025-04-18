package com.ingrap.backend.module.user.dto;

import com.ingrap.backend.module.user.domain.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "사용자 이름은 필수입니다.")
    private String username;

    private UserType userType; // 선택값. null이면 기본값 INDIVIDUAL 처리
}
