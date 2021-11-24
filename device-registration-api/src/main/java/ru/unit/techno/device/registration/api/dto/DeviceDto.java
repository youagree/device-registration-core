package ru.unit.techno.device.registration.api.dto;

import lombok.Data;
import ru.unit.techno.device.registration.api.enums.DeviceType;

@Data
public class DeviceDto {

    private Long id;
    private DeviceType type;
}
