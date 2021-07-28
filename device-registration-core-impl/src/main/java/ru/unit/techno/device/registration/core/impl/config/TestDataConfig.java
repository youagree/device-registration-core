
package ru.unit.techno.device.registration.core.impl.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;
import ru.unit.techno.device.registration.core.impl.entity.GroupsEntity;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;
import ru.unit.techno.device.registration.core.impl.repository.BarrierRepository;
import ru.unit.techno.device.registration.core.impl.repository.GroupsRepository;
import ru.unit.techno.device.registration.core.impl.repository.RfidDevicesRepository;

/**
 * TestDataConfig.
 *
 * @author Aleksey_Vasin
 */
//@Profile("local")
@Configuration
@RequiredArgsConstructor
public class TestDataConfig {

    private final GroupsRepository groupsRepository;
    private final RfidDevicesRepository rfidDevicesRepository;
    private final BarrierRepository barrierRepository;

    @Bean
    @Transactional
    public void createTestGroup() {
        rfidDevicesRepository.deleteAll();
        barrierRepository.deleteAll();
        groupsRepository.deleteAll();

        var group = groupsRepository.save(
                new GroupsEntity()
                        .setAddress("test-address")
                        .setGroupId(1L)

        );

        barrierRepository.save(new BarrierEntity()
                .setGroup(group)
                .setDeviceId(3456L)
                .setType(DeviceType.ENTRY));

        rfidDevicesRepository.save(
                new RfidDeviceEntity()
                        .setGroup(group)
                        .setDeviceId(2345L)
                        .setType(DeviceType.RFID)
        );
    }

}