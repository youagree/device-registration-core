
package ru.unit.techno.device.registration.core.impl.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.unit.techno.device.registration.api.dto.GroupsDto;
import ru.unit.techno.device.registration.core.impl.service.DeviceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ui/device")
public class GetDeviceResource {

    private final DeviceService deviceService;

    @GetMapping("/all")
    public GroupsDto getGroupWithAttachmentDevices() {
        return deviceService.getAllGroupsWithDevices();
    }
}