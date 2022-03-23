package ru.unit.techno.device.registration.core.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;
import ru.unit.techno.device.registration.core.impl.enums.RfidSubType;

import java.util.List;

public interface RfidDevicesRepository extends JpaRepository<RfidDeviceEntity, Long> {

    RfidDeviceEntity findByDeviceId(Long deviceId);

    RfidDeviceEntity findByGroup_GroupId(Long id);

    List<RfidDeviceEntity> findAllByGroup_GroupId(Long id);

    RfidDeviceEntity findByRfidSubType(RfidSubType rfidSubType);
}
