package ru.unit.techno.device.registration.core.impl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unit.techno.device.registration.api.dto.DeviceInfoDto;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit.techno.device.registration.api.dto.GroupDto;
import ru.unit.techno.device.registration.api.dto.GroupsDto;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;
import ru.unit.techno.device.registration.core.impl.entity.CardEntity;
import ru.unit.techno.device.registration.core.impl.entity.GroupsEntity;
import ru.unit.techno.device.registration.core.impl.entity.QrEntity;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;
import ru.unit.techno.device.registration.core.impl.mapper.DeviceInfoDtoMapper;
import ru.unit.techno.device.registration.core.impl.mapper.GroupsDtoMapper;
import ru.unit.techno.device.registration.core.impl.repository.BarrierRepository;
import ru.unit.techno.device.registration.core.impl.repository.CardRepository;
import ru.unit.techno.device.registration.core.impl.repository.GroupsRepository;
import ru.unit.techno.device.registration.core.impl.repository.QrRepository;
import ru.unit.techno.device.registration.core.impl.repository.RfidDevicesRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final GroupsRepository groupsRepository;
    private final RfidDevicesRepository rfidDevicesRepository;
    private final BarrierRepository barrierRepository;
    private final CardRepository cardRepository;
    private final QrRepository qrRepository;

    private final GroupsDtoMapper groupsDtoMapper;
    private final DeviceInfoDtoMapper deviceInfoDtoMapper;

    @Transactional
    public DeviceResponseDto getGroupDevices(Long deviceId, DeviceType deviceType) {
        Long groupId = null;

        switch (deviceType) {
            case QR:
                groupId = qrRepository.findByDeviceId(deviceId).getGroup().getGroupId();
                break;
            case RFID:
                groupId = rfidDevicesRepository.findByDeviceId(deviceId).getGroup().getGroupId();
                break;
            case CARD:
                groupId = cardRepository.findByDeviceId(deviceId).getGroup().getGroupId();
        }

        if (groupId != null) {
            //todo barrier inside group
            BarrierEntity groupBarrier = barrierRepository.findByGroup_GroupId(groupId);
            return new DeviceResponseDto()
                    .setDeviceId(groupBarrier.getDeviceId())
                    .setType(groupBarrier.getType().getValue())
                    .setEntryAddress(groupBarrier.getGroup().getAddress());
        }

        return null;
    }

    @Transactional
    public GroupsDto getAllGroupsWithDevices() {
        List<GroupsEntity> allGroups = groupsRepository.findAll();
        List<GroupDto> groupDtos = groupsDtoMapper.toDto(allGroups);

        groupDtos.forEach(groupDto -> {
            BarrierEntity attachedBarrier = barrierRepository.findByGroup_GroupId(groupDto.getGroupId());
            RfidDeviceEntity attachedRfid = rfidDevicesRepository.findByGroup_GroupId(groupDto.getGroupId());
            CardEntity cardEntity = cardRepository.findByGroup_GroupId(groupDto.getGroupId());
            QrEntity qrEntity = qrRepository.findByGroup_GroupId(groupDto.getGroupId());

            List<DeviceInfoDto> deviceInfoDtoList =
                    Stream.of(attachedBarrier, attachedRfid, cardEntity, qrEntity)
                            .filter(Objects::nonNull)
                            .map(this::checkTypeAndMap)
                            .collect(Collectors.toList());

            groupDto.setDeviceInfoDtoList(deviceInfoDtoList);
        });
        return new GroupsDto().setGroupDtoList(groupDtos);
    }

    //todo сделать получше
    private DeviceInfoDto checkTypeAndMap(Object o) {
        DeviceInfoDto deviceInfoDto = null;
        if (o instanceof RfidDeviceEntity) {
            RfidDeviceEntity rfidDeviceEntity = (RfidDeviceEntity) o;
            deviceInfoDto = deviceInfoDtoMapper.toDto(rfidDeviceEntity);
        } else if (o instanceof BarrierEntity) {
            BarrierEntity barrierEntity = (BarrierEntity) o;
            deviceInfoDto = deviceInfoDtoMapper.toDto(barrierEntity);
        } else if (o instanceof QrEntity) {
            QrEntity qrEntity = (QrEntity) o;
            deviceInfoDto = deviceInfoDtoMapper.toDto(qrEntity);
        } else if (o instanceof CardEntity) {
            CardEntity cardEntity = (CardEntity) o;
            deviceInfoDto = deviceInfoDtoMapper.toDto(cardEntity);
        }
        return deviceInfoDto;
    }
}
