package ru.unit.techno.device.registration.api;

import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.unit.techno.device.registration.api.dto.DeviceInfoDto;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit.techno.device.registration.api.dto.DeviceSourceTargetDto;
import ru.unit.techno.device.registration.api.enums.DeviceType;


@FeignClient(name = "device-registration-core")
public interface DeviceResource {

    @GetMapping(value = "/api/device/find/{deviceId}")
    DeviceResponseDto getGroupDevices(@PathVariable(name = "deviceId") Long deviceId, @RequestParam @QueryMap DeviceType deviceType);

    @GetMapping("/api/device/find/tableReader")
    DeviceResponseDto getReaderDeviceId();

    @GetMapping("/api/device/find/targetDevice")
    DeviceSourceTargetDto getTargetDevice(@RequestParam Long deviceId,
                                          @RequestParam @QueryMap DeviceType source,
                                          @RequestParam @QueryMap DeviceType target);
}
