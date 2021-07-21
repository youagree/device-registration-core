package ru.unit.techno.device.registration.core.impl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.unit.techno.device.registration.api.dto.DeviceRequestDto;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;
import ru.unit.techno.device.registration.core.impl.repository.BarrierRepository;
import ru.unit.techno.device.registration.core.impl.repository.RfidDevicesRepository;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final RfidDevicesRepository rfidDevicesRepository;
    private final BarrierRepository barrierRepository;

    public DeviceResponseDto getGroupDevices(DeviceRequestDto request) {
        RfidDeviceEntity rfidDevice = rfidDevicesRepository.findByDeviceId(request.getDeviceId());

        if (rfidDevice != null) {
            Long groupId = rfidDevice.getGroup().getGroupId();
            BarrierEntity groupBarrier = barrierRepository.findByGroup_GroupId(groupId);
            return new DeviceResponseDto()
                    .setDeviceId(groupBarrier.getDeviceId())
                    .setType(groupBarrier.getType().getValue())
                    .setEntryAddress(groupBarrier.getGroup().getAddress());
        }

        return null;
    }
}
