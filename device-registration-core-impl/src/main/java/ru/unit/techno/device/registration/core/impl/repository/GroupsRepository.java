package ru.unit.techno.device.registration.core.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unit.techno.device.registration.core.impl.entity.GroupsEntity;

public interface GroupsRepository extends JpaRepository<GroupsEntity, Long> {
}
