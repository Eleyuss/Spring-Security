package com.gbv.webapp_gbv_l3.entity;

public enum RoleEnum {
    ADMIN("ADMIN"),
    USER("USER");

    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

