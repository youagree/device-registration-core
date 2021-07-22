package ru.unit.techno.device.registration.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.unit.techno.device.registration.api.dto.DeviceRequestDto;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;


@FeignClient(name = "device-registration-core")
public interface DeviceResource {

    @GetMapping(value = "/api/device/find/{deviceId}")
    DeviceResponseDto getGroupDevices(@PathVariable(name = "deviceId") Long deviceId);
}
