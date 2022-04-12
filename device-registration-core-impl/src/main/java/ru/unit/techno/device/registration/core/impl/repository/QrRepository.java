
package ru.unit.techno.device.registration.core.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unit.techno.device.registration.core.impl.entity.QrEntity;

import java.util.List;

public interface QrRepository extends JpaRepository<QrEntity, Long> {

    QrEntity findByGroup_GroupId(String id);

    QrEntity findByDeviceId(Long deviceId);

    List<QrEntity> findAllByGroup_GroupId(String id);
}