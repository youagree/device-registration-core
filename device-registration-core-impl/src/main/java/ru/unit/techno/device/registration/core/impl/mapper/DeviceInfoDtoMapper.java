
package ru.unit.techno.device.registration.core.impl.mapper;

import org.mapstruct.Mapper;
import ru.unit.techno.device.registration.api.dto.DeviceInfoDto;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;
import ru.unit.techno.device.registration.core.impl.entity.CardEntity;
import ru.unit.techno.device.registration.core.impl.entity.QrEntity;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;

@Mapper
public interface DeviceInfoDtoMapper {
    DeviceInfoDto toDto(RfidDeviceEntity barrierEntity);

    DeviceInfoDto toDto(CardEntity cardEntity);

    DeviceInfoDto toDto(QrEntity qrEntity);

    DeviceInfoDto toDto(BarrierEntity qrEntity);
}