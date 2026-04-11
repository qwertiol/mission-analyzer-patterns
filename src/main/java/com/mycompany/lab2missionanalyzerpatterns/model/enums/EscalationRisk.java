package com.mycompany.lab2missionanalyzerpatterns.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EscalationRisk {
    HIGH, MEDIUM, LOW, UNKNOWN;

    @JsonCreator
    public static EscalationRisk fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return EscalationRisk.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}