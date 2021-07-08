
package ru.unit.techno.device.registration.core.impl.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import ru.unit.techno.device.registration.core.impl.base.BaseTestClass;
import ru.unit.techno.device.registration.core.impl.dto.DeviceDto;
import ru.unit.techno.device.registration.core.impl.dto.RegistrationDto;
import ru.unit.techno.device.registration.core.impl.entity.BarrierEntity;
import ru.unit.techno.device.registration.core.impl.entity.GroupsEntity;
import ru.unit.techno.device.registration.core.impl.entity.RfidDeviceEntity;
import ru.unit.techno.device.registration.core.impl.service.RegistrationService;

import java.util.List;

public class RegistrationControllerTest extends BaseTestClass {

    public static final String BASE_URL = "/device-registration-core/api";
    public static final String REGISTER = "/registration";

    @Autowired
    private RegistrationService registrationService;

    @Test
    @DisplayName("когда устройства зарегались успешно")
    public void successRegistrationTest() {
        var input = buildRegisterDto();

        var groupIdFromBody = testUtils.invokePostApi(Long.class, BASE_URL + REGISTER, HttpStatus.CREATED, input);

        List<GroupsEntity> all = groupsRepository.findAll();
        assertEquals(all.size(), 1);

        GroupsEntity groupsEntity = all.get(0);
        var groupId = groupsEntity.getGroupId();

        BarrierEntity barrierEntity = barrierRepository.findByGroup_GroupId(groupId);
        assertEquals(barrierEntity.getDeviceId(), 2L);

        RfidDeviceEntity rfidEntity = rfidDevicesRepository.findByGroup_GroupId(groupId);
        assertEquals(rfidEntity.getDeviceId(), 2L);

        assertEquals(groupIdFromBody, groupsEntity.getGroupId());
    }

    private RegistrationDto buildRegisterDto() {
        return new RegistrationDto()
                .setAddress("127.0.0.1")
                .setGroup(null)
                .setGroups(List.of(new DeviceDto()
                                .setId(1L)
                                .setType("RFID"),
                        new DeviceDto()
                                .setId(2L)
                                .setType("ENTRY")));
    }
}