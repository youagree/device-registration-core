
package ru.unit.techno.device.registration.core.impl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.unit.techno.device.registration.api.dto.GroupDto;
import ru.unit.techno.device.registration.core.impl.entity.GroupsEntity;

import java.util.List;

@Mapper
public interface GroupsDtoMapper {
    List<GroupDto> toDto(Iterable<GroupsEntity> groupsEntities);

    @Mapping(target = "deviceInfoDtoList", ignore = true)
    GroupDto toGroupDto(GroupsEntity groupsEntities);
}