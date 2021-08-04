package ru.unit.techno.device.registration.core.impl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.unit.techno.device.registration.api.dto.DeviceInfoDto;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit.techno.device.registration.api.dto.GroupDto;
import ru.unit.techno.device.registration.api.dto.GroupsDto;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;
import ru.unit.techno.device.registration.core.impl.entity.GroupsEntity;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;
import ru.unit.techno.device.registration.core.impl.mapper.DeviceInfoDtoMapper;
import ru.unit.techno.device.registration.core.impl.mapper.GroupsDtoMapper;
import ru.unit.techno.device.registration.core.impl.repository.BarrierRepository;
import ru.unit.techno.device.registration.core.impl.repository.GroupsRepository;
import ru.unit.techno.device.registration.core.impl.repository.RfidDevicesRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private GroupsRepository groupsRepository;
    private final RfidDevicesRepository rfidDevicesRepository;
    private final BarrierRepository barrierRepository;
    private final GroupsDtoMapper groupsDtoMapper;
    private final DeviceInfoDtoMapper deviceInfoDtoMapper;

    public DeviceResponseDto getGroupDevices(Long deviceId) {
        RfidDeviceEntity rfidDevice = rfidDevicesRepository.findByDeviceId(deviceId);

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

    // TODO: 03.08.2021 покрыть тестами 
    public GroupsDto getAllGroupsWithDevices() {
        List<GroupsEntity> allGroups = groupsRepository.findAll();
        List<GroupDto> groupDtos = groupsDtoMapper.toDto(allGroups);

        groupDtos.forEach(groupDto -> {
            List<DeviceInfoDto> deviceInfoDtoList = new ArrayList<>();
            BarrierEntity attachedBarrier = barrierRepository.findByGroup_GroupId(groupDto.getId());
            RfidDeviceEntity attachedRfid = rfidDevicesRepository.findByGroup_GroupId(groupDto.getId());
            List<DeviceInfoDto> attachedDevices =
                    Arrays.asList(deviceInfoDtoMapper.toDto(attachedBarrier), deviceInfoDtoMapper.toDto(attachedRfid));
            deviceInfoDtoList.addAll(attachedDevices);
            groupDto.setDeviceInfoDtoList(deviceInfoDtoList);
        });
        return new GroupsDto().setGroupDtoList(groupDtos);
    }
}
