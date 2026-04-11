package com.mycompany.lab2missionanalyzerpatterns.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ThreatLevel {
    HIGH, SPECIAL_GRADE, MEDIUM, LOW, UNKNOWN;

    @JsonCreator
    public static ThreatLevel fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return ThreatLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}