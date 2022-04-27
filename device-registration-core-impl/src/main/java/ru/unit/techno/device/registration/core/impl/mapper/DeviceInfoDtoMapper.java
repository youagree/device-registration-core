
package ru.unit.techno.device.registration.core.impl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.unit.techno.device.registration.api.dto.DeviceInfoDto;
import ru.unit.techno.device.registration.api.dto.DeviceSourceTargetDto;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;
import ru.unit.techno.device.registration.core.impl.entity.CardEntity;
import ru.unit.techno.device.registration.core.impl.entity.QrEntity;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;

@Mapper
public interface DeviceInfoDtoMapper {
    @Mapping(target = "address", source = "group.address")
    DeviceInfoDto toDto(RfidDeviceEntity barrierEntity);

    @Mapping(target = "address", source = "group.address")
    DeviceInfoDto toDto(CardEntity cardEntity);

    @Mapping(target = "address", source = "group.address")
    DeviceInfoDto toDto(QrEntity qrEntity);

    @Mapping(target = "address", source = "group.address")
    DeviceInfoDto toDto(BarrierEntity qrEntity);

    DeviceSourceTargetDto toTarget(DeviceInfoDto deviceInfoDto);
}