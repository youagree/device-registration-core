package ru.unit.techno.device.registration.api.enums;

import lombok.Getter;

@Getter
public enum DeviceType {
    ENTRY("ENTRY"), RFID("RFID"), QR("QR"), CARD("CARD");

    @Getter
    private final String value;

    DeviceType(String value) {
        this.value = value;
    }
}
