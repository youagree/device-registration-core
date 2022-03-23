package ru.unit.techno.device.registration.api.dto;

import lombok.Data;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit.techno.device.registration.api.enums.SubType;

@Data
public class DeviceDto {

    private Long id;
    private DeviceType type;
    private SubType subType;
}
