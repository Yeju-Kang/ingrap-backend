package com.ingrap.backend.module.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {
    INDIVIDUAL,
    COMPANY;

    @JsonCreator
    public static UserType from(String value) {
        return UserType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase(); // 응답 시 "individual", "company" 로 내려감
    }
}
