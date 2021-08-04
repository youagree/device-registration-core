package ru.unit.techno.device.registration.api.dto;

import lombok.Data;

@Data
public class DeviceRequestDto {
    private Long deviceId;
    private String type;
}
