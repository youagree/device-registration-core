package ru.unit.techno.device.registration.core.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;

import java.util.List;

public interface BarrierRepository extends JpaRepository<BarrierEntity, Long> {

    BarrierEntity findByDeviceId(Long deviceId);

    BarrierEntity findByGroup_GroupId(Long id);

    List<BarrierEntity> findAllByGroup_GroupId(Long id);
}
