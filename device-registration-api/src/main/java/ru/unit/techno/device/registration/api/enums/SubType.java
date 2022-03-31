package ru.unit.techno.device.registration.api.enums;

import lombok.Getter;

public enum SubType {
    TABLE_READER("ENTRY_READER"),
    ENTRY_READER("ENTRY_READER"),
    UNKNOWN("");

    @Getter
    private final String value;

    SubType(String value) {
        this.value = value;
    }
}
