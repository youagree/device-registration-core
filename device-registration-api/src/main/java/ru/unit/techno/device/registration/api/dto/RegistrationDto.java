package ru.unit.techno.device.registration.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegistrationDto {

    private String address;
    private Long group;
    private List<DeviceDto> groups;
}
