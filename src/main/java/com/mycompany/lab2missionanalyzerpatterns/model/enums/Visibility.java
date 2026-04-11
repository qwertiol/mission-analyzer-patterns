package com.mycompany.lab2missionanalyzerpatterns.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Visibility {
    HIGH, MEDIUM, LOW, UNKNOWN;

    @JsonCreator
    public static Visibility fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return Visibility.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}