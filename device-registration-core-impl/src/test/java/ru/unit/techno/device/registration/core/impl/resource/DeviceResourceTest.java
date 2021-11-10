package ru.unit.techno.device.registration.core.impl.resource;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import ru.unit.techno.device.registration.api.dto.GroupsDto;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit.techno.device.registration.core.impl.base.BaseTestClass;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;
import ru.unit.techno.device.registration.core.impl.entity.GroupsEntity;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;

public class DeviceResourceTest extends BaseTestClass {

    private static final String BASE_URL = "/ui/device";

    @BeforeEach
    public void initData() {
        GroupsEntity gr1 = groupsRepository.save(new GroupsEntity()
                .setGroupId(5555L)
                .setAddress("Zalupa"));

        GroupsEntity gr2 = groupsRepository.save(new GroupsEntity()
                .setGroupId(7777L)
                .setAddress("Jopa"));

        rfidDevicesRepository.save(new RfidDeviceEntity().setDeviceId(123L).setGroup(gr1).setType(DeviceType.RFID));
        barrierRepository.save(new BarrierEntity().setDeviceId(456L).setGroup(gr1).setType(DeviceType.ENTRY));

        rfidDevicesRepository.save(new RfidDeviceEntity().setDeviceId(789L).setGroup(gr2).setType(DeviceType.RFID));
        barrierRepository.save(new BarrierEntity().setDeviceId(555L).setGroup(gr2).setType(DeviceType.ENTRY));
    }

    @Test
    @DisplayName("Получение всех групп с устройствами")
    public void getAllGroupsTest() {
        String url = BASE_URL + "/all";

        var result = testUtils.invokeGetApi(new ParameterizedTypeReference<GroupsDto>() {}, url, HttpStatus.OK);
        Assertions.assertEquals(result.getGroupDtoList().size(), 2);
    }
}
