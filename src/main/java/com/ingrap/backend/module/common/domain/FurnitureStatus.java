package com.ingrap.backend.module.common.domain;

public enum FurnitureStatus {
    NEW("새로 등록됨"),
    REGISTERED("등록 완료"),
    DELETED("삭제됨");

    private final String description;

    FurnitureStatus(String description) {
        this.description = description;
    }

    /**
     * 한글 설명을 가져옵니다.
     */
    public String getDescription() {
        return description;
    }
}