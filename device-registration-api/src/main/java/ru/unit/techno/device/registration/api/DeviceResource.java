package ru.unit.techno.device.registration.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.unit.techno.device.registration.api.dto.DeviceRequestDto;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;


@FeignClient(name = "device-registration-core")
@RequestMapping("/api/device/find")
public interface DeviceResource {

    DeviceResponseDto getGroupDevices(DeviceRequestDto request);
}
