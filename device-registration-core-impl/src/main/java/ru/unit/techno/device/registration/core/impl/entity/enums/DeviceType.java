package ru.unit.techno.device.registration.core.impl.entity.enums;

import lombok.Getter;

@Getter
public enum DeviceType {
    ENTRY("ENTRY"), RFID("RFID"), QR("QR");

    @Getter
    private String value;

    DeviceType(String value) {
        this.value = value;
    }
}
