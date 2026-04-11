package com.mycompany.lab2missionanalyzerpatterns.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MissionOutcome {
    SUCCESS, PARTIAL_SUCCESS, FAILURE, UNKNOWN;

    @JsonCreator
    public static MissionOutcome fromString(String value) {
        if (value == null) return UNKNOWN;
        try {
            return MissionOutcome.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

}