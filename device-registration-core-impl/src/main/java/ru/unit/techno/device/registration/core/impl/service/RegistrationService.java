package ru.unit.techno.device.registration.core.impl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.unit.techno.device.registration.api.dto.DeviceDto;
import ru.unit.techno.device.registration.api.dto.RegistrationDto;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit.techno.device.registration.core.impl.entity.*;
import ru.unit.techno.device.registration.core.impl.enums.RfidSubType;
import ru.unit.techno.device.registration.core.impl.repository.*;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final GroupsRepository groupsRepository;
    private final RfidDevicesRepository rfidDevicesRepository;
    private final BarrierRepository barrierRepository;
    private final QrRepository qrRepository;
    private final CardRepository cardRepository;

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

        groupsRepository.saveAndFlush(groupsEntity);
    }

    private Long findRegisteredDevices(List<DeviceDto> devices) {
        Long groupId = null;

        for (DeviceDto dev : devices) {
            /// TODO: 11.11.2021 Мб как то унифицировать функционал кейсов, чтобы 4 почти одинаковых кейса обрабатывать. Мб через дженерики, хз
            String deviceType = dev.getType().getValue();
            switch (deviceType) {
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

                case ("QR"):
                    QrEntity existQr = qrRepository.findByDeviceId(dev.getId());
                    if (existQr != null) {
                        groupId = existQr.getGroup().getGroupId();
                        return groupId;
                    }
                    break;

                case ("CARD"):
                    CardEntity existCar = cardRepository.findByDeviceId(dev.getId());
                    if (existCar != null) {
                        groupId = existCar.getGroup().getGroupId();
                        return groupId;
                    }
                    break;
            }
        }

        return groupId;
    }

    private void parseRegistrationRequestAndSaveDevices(List<DeviceDto> devices, GroupsEntity groupId) {

        for (DeviceDto deviceDto : devices) {
            String deviceType = deviceDto.getType().getValue();

            String deviceSubType = "UNKNOWN";
            if (deviceDto.getSubType() != null) {
                deviceSubType = deviceDto.getSubType().getValue();
            }

            switch (deviceType) {
                case ("RFID"):
                    if (!checkRfidDevice(deviceDto.getId())) {
                        RfidDeviceEntity rfidEntity = new RfidDeviceEntity();
                        rfidEntity.setDeviceId(deviceDto.getId())
                                .setGroup(groupId)
                                .setType(DeviceType.RFID)
                                .setRfidSubType(RfidSubType.valueOf(deviceSubType));
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

                case ("QR"):
                    if (!checkQrDevice(deviceDto.getId())) {
                        QrEntity qrEntity = new QrEntity();
                        qrEntity.setDeviceId(deviceDto.getId())
                                .setGroup(groupId)
                                .setType(DeviceType.QR);
                        qrRepository.save(qrEntity);
                    }
                    break;

                case ("CARD"):
                    if (!checkCardDevice(deviceDto.getId())) {
                        CardEntity cardEntity = new CardEntity();
                        cardEntity.setDeviceId(deviceDto.getId())
                                .setGroup(groupId)
                                .setType(DeviceType.CARD);
                        cardRepository.save(cardEntity);
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

    private boolean checkQrDevice(Long deviceId) {
        QrEntity byDeviceId = qrRepository.findByDeviceId(deviceId);
        if (byDeviceId != null) {
            log.info("This device is already registered in service");
            return true;
        }

        return false;
    }

    private boolean checkCardDevice(Long deviceId) {
        CardEntity byDeviceId = cardRepository.findByDeviceId(deviceId);
        if (byDeviceId != null) {
            log.info("This device is already registered in service");
            return true;
        }

        return false;
    }
}
