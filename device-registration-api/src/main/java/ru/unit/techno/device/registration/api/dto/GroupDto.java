
package ru.unit.techno.device.registration.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupDto {
    private String groupId;
    private List<DeviceInfoDto> deviceInfoDtoList;
}