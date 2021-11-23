package ru.unit.techno.device.registration.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class RegistrationDto {

    @NotBlank
    private String address;
    private Long group;
    private List<DeviceDto> groups;
}
