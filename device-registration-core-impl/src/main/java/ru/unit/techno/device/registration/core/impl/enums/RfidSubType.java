package ru.unit.techno.device.registration.core.impl.enums;

import lombok.Getter;

public enum RfidSubType {
    TABLE_READER("TABLE_READER"),
    ENTRY_READER("ENTRY_READER"),
    UNKNOWN("");

    @Getter
    private final String value;

    RfidSubType(String value) {
        this.value = value;
    }
}
