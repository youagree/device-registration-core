package ru.unit.techno.device.registration.core.impl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.unit.techno.device.registration.api.dto.DeviceDto;
import ru.unit.techno.device.registration.api.dto.RegistrationDto;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;
import ru.unit.techno.device.registration.core.impl.entity.GroupsEntity;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit.techno.device.registration.core.impl.repository.BarrierRepository;
import ru.unit.techno.device.registration.core.impl.repository.GroupsRepository;
import ru.unit.techno.device.registration.core.impl.repository.RfidDevicesRepository;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final GroupsRepository groupsRepository;
    private final RfidDevicesRepository rfidDevicesRepository;
    private final BarrierRepository barrierRepository;

    @Transactional
    public void registerGroup(RegistrationDto registrationDto) {
        GroupsEntity groupsEntity;
        //TODO Проверить были ли регистрирующиеся устройства в какой либо группе

        if (registrationDto.getGroup() == null) {
            Long existedGroup = findRegisteredDevices(registrationDto.getGroups());
            if (existedGroup != null) {
                groupsEntity = groupsRepository.findByGroupId(existedGroup);
            } else {
                throw new IllegalArgumentException("A group belonging to these devices was not found and the group ID in the request is null");
            }
        } else {
            groupsEntity = groupsRepository.findByGroupId(registrationDto.getGroup());
            if (groupsEntity == null) {
                groupsEntity = new GroupsEntity()
                        .setAddress(registrationDto.getAddress())
                        .setGroupId(registrationDto.getGroup());

                groupsRepository.saveAndFlush(groupsEntity);
            }

            if (!CollectionUtils.isEmpty(registrationDto.getGroups())) {
                parseRegistrationRequestAndSaveDevices(registrationDto.getGroups(), groupsEntity);
            } else {
                throw new IllegalArgumentException("Cant register devices if devices count is 0");
            }
        }

        groupsEntity.setAddress(registrationDto.getAddress());
        groupsRepository.saveAndFlush(groupsEntity);
    }

    private Long findRegisteredDevices(List<DeviceDto> devices) {
        Long groupId = null;

        for (DeviceDto dev : devices) {
            switch (dev.getType()) {
                case ("RFID"):
                    RfidDeviceEntity existRfid = rfidDevicesRepository.findByDeviceId(dev.getId());
                    if (existRfid != null) {
                        groupId = existRfid.getGroup().getGroupId();
                        return groupId;
                    }
                    break;

                case ("ENTRY"):
                    BarrierEntity existBarrier = barrierRepository.findByDeviceId(dev.getId());

                    if (existBarrier != null) {
                        groupId = existBarrier.getGroup().getGroupId();
                        return groupId;
                    }
                    break;
            }
        }

        return groupId;
    }

    private void parseRegistrationRequestAndSaveDevices(List<DeviceDto> devices, GroupsEntity groupId) {

        for (DeviceDto deviceDto : devices) {
            switch (deviceDto.getType()) {
                case ("RFID"):
                    if (!checkRfidDevice(deviceDto.getId())) {
                        RfidDeviceEntity rfidEntity = new RfidDeviceEntity();
                        rfidEntity.setDeviceId(deviceDto.getId())
                                .setGroup(groupId)
                                .setType(DeviceType.RFID);
                        rfidDevicesRepository.save(rfidEntity);
                    }
                    break;
                case ("ENTRY"):
                    if (!checkBarrierDevice(deviceDto.getId())) {
                        BarrierEntity barrierEntity = new BarrierEntity();
                        barrierEntity.setDeviceId(deviceDto.getId())
                                .setGroup(groupId)
                                .setType(DeviceType.ENTRY);
                        barrierRepository.save(barrierEntity);
                    }
                    break;
            }
        }
    }

    private boolean checkRfidDevice(Long deviceId) {
        RfidDeviceEntity byDeviceId = rfidDevicesRepository.findByDeviceId(deviceId);
        if (byDeviceId != null) {
            log.info("This device is already registered in service");
            return true;
        }
        return false;
    }

    private boolean checkBarrierDevice(Long deviceId) {
        BarrierEntity byDeviceId = barrierRepository.findByDeviceId(deviceId);
        if (byDeviceId != null) {
            log.info("This device is already registered in service");
            return true;
        }

        return false;
    }
}
