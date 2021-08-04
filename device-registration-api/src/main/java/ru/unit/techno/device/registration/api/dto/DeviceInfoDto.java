
package ru.unit.techno.device.registration.api.dto;


import lombok.Data;
import ru.unit.techno.device.registration.api.enums.DeviceType;

@Data
public class DeviceInfoDto {
    private Long id;
    private Long deviceId;
    private DeviceType type;
}