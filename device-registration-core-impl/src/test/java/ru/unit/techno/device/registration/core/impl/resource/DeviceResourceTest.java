package ru.unit.techno.device.registration.core.impl.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import ru.unit.techno.device.registration.api.dto.DeviceInfoDto;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit.techno.device.registration.api.dto.DeviceSourceTargetDto;
import ru.unit.techno.device.registration.api.dto.GroupsDto;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit.techno.device.registration.core.impl.base.BaseTestClass;
import ru.unit.techno.device.registration.core.impl.entity.*;
import ru.unit.techno.device.registration.core.impl.enums.RfidSubType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeviceResourceTest extends BaseTestClass {

    private static final String BASE_URL = "/ui/device";

    @BeforeEach
    public void initData() {
        GroupsEntity gr1 = groupsRepository.save(new GroupsEntity()
                .setGroupId("5555L")
                .setAddress("Zalupa"));

        GroupsEntity gr2 = groupsRepository.save(new GroupsEntity()
                .setGroupId("7777L")
                .setAddress("Jopa"));

        rfidDevicesRepository.save(new RfidDeviceEntity().setDeviceId(123L).setGroup(gr1).setType(DeviceType.RFID).setRfidSubType(RfidSubType.TABLE_READER));
        barrierRepository.save(new BarrierEntity().setDeviceId(456L).setGroup(gr1).setType(DeviceType.ENTRY));
        qrRepository.save(new QrEntity().setDeviceId(777L).setGroup(gr1).setType(DeviceType.QR));
        cardRepository.save(new CardEntity().setDeviceId(918L).setGroup(gr1).setType(DeviceType.CARD));

        rfidDevicesRepository.save(new RfidDeviceEntity().setDeviceId(789L).setGroup(gr2).setType(DeviceType.RFID));
        barrierRepository.save(new BarrierEntity().setDeviceId(555L).setGroup(gr2).setType(DeviceType.ENTRY));
        qrRepository.save(new QrEntity().setDeviceId(744L).setGroup(gr2).setType(DeviceType.QR));
        cardRepository.save(new CardEntity().setDeviceId(1231112L).setGroup(gr2).setType(DeviceType.CARD));
    }

    @Test
    @DisplayName("Получение всех групп с устройствами")
    public void getAllGroupsTest() {
        String url = BASE_URL + "/all";

        var result = testUtils.invokeGetApi(new ParameterizedTypeReference<GroupsDto>() {
        }, url, HttpStatus.OK);
        assertEquals(result.getGroupDtoList().size(), 2);
    }

    @Test
    @DisplayName("Получение настольного ридера")
    public void getReaderDeviceId() {
        String url = "/api/device/find/tableReader";

        var result = testUtils.invokeGetApi(new ParameterizedTypeReference<DeviceResponseDto>() {
        }, url, HttpStatus.OK);

        assertEquals(result.getDeviceId(), 123L);
    }

    @Test
    @DisplayName("Тест на получения конкретного устройства в группе по DeviceType")
    public void getTargetDevice() {
        String url = "/api/device/find/targetDevice";

        DeviceSourceTargetDto deviceInfoDto = testUtils.invokeGetApi(new ParameterizedTypeReference<DeviceSourceTargetDto>() {},
                url + "?deviceId={1}&source={2}&target={3}",
                HttpStatus.OK,
                777L, DeviceType.QR, DeviceType.CARD);

        assertEquals(deviceInfoDto.getAddress(), "Zalupa");
        assertEquals(deviceInfoDto.getDeviceId(), 918L);
    }
}
