package com.ingrap.backend.module.common.domain;

public enum FurnitureType {
    CHAIR("의자"),
    TABLE("테이블"),
    BED("침대"),
    SOFA("소파"),
    DESK("책상"),
    CABINET("캐비닛"),
    SHELF("선반");

    private final String description;

    FurnitureType(String description) {
        this.description = description;
    }

    /**
     * 한글 설명을 가져옵니다.
     */
    public String getDescription() {
        return description;
    }
}
