package ru.unit.techno.device.registration.core.impl.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.unit.techno.device.registration.api.dto.DeviceInfoDto;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit.techno.device.registration.api.dto.DeviceSourceTargetDto;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit.techno.device.registration.core.impl.service.DeviceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/device/find")
public class DeviceStorageResource {

    private final DeviceService deviceService;

    @GetMapping("/{deviceId}")
    public DeviceResponseDto getGroupDevices(@PathVariable(name = "deviceId") Long deviceId, @RequestParam DeviceType deviceType) {
        return deviceService.getGroupDevices(deviceId, deviceType);
    }

    @GetMapping("/tableReader")
    public DeviceResponseDto getReaderDeviceId() {
        return deviceService.getReaderDeviceId();
    }

    @GetMapping("/targetDevice")
    public DeviceSourceTargetDto getTargetDevice(@RequestParam Long deviceId, @RequestParam DeviceType source, @RequestParam DeviceType target) {
        return deviceService.findTargetDevice(deviceId, source, target);
    }

    @PostMapping("/link-tag-id")
    public void linkGroupToTag(
            @RequestParam("tagId") String tagId,
            @RequestParam("groupId") String groupId
    ) {
        deviceService.linkToTag(tagId, groupId);
    }
}
