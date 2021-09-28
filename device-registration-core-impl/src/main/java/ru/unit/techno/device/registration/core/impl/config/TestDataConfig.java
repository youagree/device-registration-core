
package ru.unit.techno.device.registration.core.impl.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;
import ru.unit.techno.device.registration.core.impl.entity.CardEntity;
import ru.unit.techno.device.registration.core.impl.entity.GroupsEntity;
import ru.unit.techno.device.registration.core.impl.entity.QrEntity;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;
import ru.unit.techno.device.registration.core.impl.repository.BarrierRepository;
import ru.unit.techno.device.registration.core.impl.repository.CardRepository;
import ru.unit.techno.device.registration.core.impl.repository.GroupsRepository;
import ru.unit.techno.device.registration.core.impl.repository.QrRepository;
import ru.unit.techno.device.registration.core.impl.repository.RfidDevicesRepository;

@Profile("!test")
@Configuration
@RequiredArgsConstructor
public class TestDataConfig {

    private final GroupsRepository groupsRepository;
    private final RfidDevicesRepository rfidDevicesRepository;
    private final BarrierRepository barrierRepository;
    private final QrRepository qrRepository;
    private final CardRepository cardRepository;

    @Bean
    @Profile("local")
    @Transactional
    //hardcode real devices
    public void createTestGroup() {
        rfidDevicesRepository.deleteAll();
        barrierRepository.deleteAll();
        qrRepository.deleteAll();
        cardRepository.deleteAll();
        groupsRepository.deleteAll();


        //group 1 https://my-atlassian-site-430.atlassian.net/wiki/spaces/CRIMEAIT/pages/88997889/REGISTRATION+MAP
        var group1 = groupsRepository.save(
                new GroupsEntity()
                        .setAddress("unknown")
                        .setGroupId(1L)
        );

        rfidDevicesRepository.save(
                new RfidDeviceEntity()
                        .setGroup(group1)
                        .setDeviceId(5463L)
                        .setType(DeviceType.RFID)
        );

        barrierRepository.save(
                new BarrierEntity()
                        .setGroup(group1)
                        .setDeviceId(1239L)
                        .setType(DeviceType.ENTRY)
        );

        qrRepository.save(
                new QrEntity()
                        .setGroup(group1)
                        .setDeviceId(7765L)
                        .setType(DeviceType.QR)
        );


        cardRepository.save(
                new CardEntity()
                        .setGroup(group1)
                        .setDeviceId(8765L)
                        .setType(DeviceType.CARD)
        );

        //group 2
        var group2 = groupsRepository.save(
                new GroupsEntity()
                        .setAddress("unknown")
                        .setGroupId(2L)
        );

        barrierRepository.save(new BarrierEntity()
                .setGroup(group2)
                .setDeviceId(9853L)
                .setType(DeviceType.ENTRY));

        //group 3
        var group3 = groupsRepository.save(
                new GroupsEntity()
                        .setAddress("unknown")
                        .setGroupId(3L)
        );

        rfidDevicesRepository.save(
                new RfidDeviceEntity()
                        .setGroup(group3)
                        .setDeviceId(4351L)
                        .setType(DeviceType.RFID)
        );

        barrierRepository.save(
                new BarrierEntity()
                        .setGroup(group3)
                        .setDeviceId(8983L)
                        .setType(DeviceType.ENTRY)
        );

        cardRepository.save(
                new CardEntity()
                        .setGroup(group3)
                        .setDeviceId(1127L)
                        .setType(DeviceType.CARD)
        );

        //group 4
        var group4 = groupsRepository.save(
                new GroupsEntity()
                        .setAddress("unknown")
                        .setGroupId(4L)
        );

        rfidDevicesRepository.save(
                new RfidDeviceEntity()
                        .setGroup(group3)
                        .setDeviceId(6354L)
                        .setType(DeviceType.RFID)
        );

        barrierRepository.save(
                new BarrierEntity()
                        .setGroup(group3)
                        .setDeviceId(2306L)
                        .setType(DeviceType.ENTRY)
        );

        cardRepository.save(
                new CardEntity()
                        .setGroup(group3)
                        .setDeviceId(7362L)
                        .setType(DeviceType.CARD)
        );

        barrierRepository.save(new BarrierEntity()
                .setGroup(group2)
                .setDeviceId(7777L)
                .setType(DeviceType.ENTRY));

        rfidDevicesRepository.save(
                new RfidDeviceEntity()
                        .setGroup(group2)
                        .setDeviceId(4334L)
                        .setType(DeviceType.RFID)
        );
    }
}