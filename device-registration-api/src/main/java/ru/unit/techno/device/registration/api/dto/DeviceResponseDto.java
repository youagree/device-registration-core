package ru.unit.techno.device.registration.api.dto;

import lombok.Data;

@Data
public class DeviceResponseDto {

    private Long deviceId;
    private String type;
    private String entryAddress;
}
