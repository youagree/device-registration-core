package ru.unit.techno.device.registration.core.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;

import java.util.List;

public interface RfidDevicesRepository extends JpaRepository<RfidDeviceEntity, Long> {

    RfidDeviceEntity findByDeviceId(Long deviceId);

    RfidDeviceEntity findByGroup_GroupId(Long id);

    List<RfidDeviceEntity> findAllByGroup_GroupId(Long id);
}
