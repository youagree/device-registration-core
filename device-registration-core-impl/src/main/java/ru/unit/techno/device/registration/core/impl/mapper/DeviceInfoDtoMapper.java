
package ru.unit.techno.device.registration.core.impl.mapper;

import org.mapstruct.Mapper;
import ru.unit.techno.device.registration.api.dto.DeviceInfoDto;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;

@Mapper
public interface DeviceInfoDtoMapper {
    DeviceInfoDto toDto(BarrierEntity barrierEntity);

    DeviceInfoDto toDto(RfidDeviceEntity barrierEntity);
}