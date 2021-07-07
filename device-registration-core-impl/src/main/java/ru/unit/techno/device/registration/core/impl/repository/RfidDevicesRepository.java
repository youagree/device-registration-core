package ru.unit.techno.device.registration.core.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;

public interface RfidDevicesRepository extends JpaRepository<RfidDeviceEntity, Long> {

    RfidDeviceEntity findByDeviceId(Long deviceId);
}
