package ru.unit.techno.device.registration.core.impl.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.unit.techno.device.registration.api.dto.DeviceRequestDto;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit.techno.device.registration.core.impl.service.DeviceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/device/find/{deviceId}")
public class DeviceStorageResource {

    private final DeviceService deviceService;

    @GetMapping
    public DeviceResponseDto getGroupDevices(@PathVariable(name = "deviceId") Long deviceId) {
        return deviceService.getGroupDevices(deviceId);
    }
}
