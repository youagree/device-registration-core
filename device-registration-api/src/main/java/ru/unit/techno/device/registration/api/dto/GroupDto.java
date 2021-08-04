
package ru.unit.techno.device.registration.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupDto {
    private Long id;
    private Long groupId;
    private List<DeviceInfoDto> deviceInfoDtoList;
}