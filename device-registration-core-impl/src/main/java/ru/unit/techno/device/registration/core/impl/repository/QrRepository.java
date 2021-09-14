
package ru.unit.techno.device.registration.core.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unit.techno.device.registration.core.impl.entity.QrEntity;

public interface QrRepository extends JpaRepository<QrEntity, Long> {

    QrEntity findByGroup_GroupId(Long id);
}