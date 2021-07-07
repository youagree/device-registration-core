package ru.unit.techno.device.registration.core.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.unit.techno.device.registration.core.impl.entity.GroupsEntity;

public interface GroupsRepository extends JpaRepository<GroupsEntity, Long> {

    @Query("select g from GroupsEntity g where g.groupId = :groupId")
    boolean isGroupExist(@Param("groupId") Long groupId);

    GroupsEntity findByGroupId(Long groupId);
}
