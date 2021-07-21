package ru.unit.techno.device.registration.core.impl.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.unit.techno.device.registration.api.dto.DeviceRequestDto;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit.techno.device.registration.core.impl.service.DeviceService;

@RequiredArgsConstructor
@RequestMapping("/api/device/find")
public class DeviceStorageResource {

    private final DeviceService deviceService;

    @GetMapping
    public DeviceResponseDto getGroupDevices(@RequestBody DeviceRequestDto request) {
        return deviceService.getGroupDevices(request);
    }
}
