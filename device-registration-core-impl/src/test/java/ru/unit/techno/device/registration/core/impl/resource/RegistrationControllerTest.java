
package ru.unit.techno.device.registration.core.impl.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import ru.unit.techno.device.registration.api.dto.DeviceDto;
import ru.unit.techno.device.registration.api.dto.RegistrationDto;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit.techno.device.registration.core.impl.base.BaseTestClass;
import ru.unit.techno.device.registration.core.impl.entity.*;
import ru.unit.techno.device.registration.core.impl.service.RegistrationService;

import java.sql.SQLException;
import java.util.List;

public class RegistrationControllerTest extends BaseTestClass {

    public static final String BASE_URL = "/api";
    public static final String REGISTER = "/registration";

    @Autowired
    private RegistrationService registrationService;

    @Test
    @DisplayName("когда устройства зарегались успешно")
    public void successRegistrationTest() {
        var input = buildRegisterDtoTwoDevices();

        var groupIdFromBody = testUtils.invokePostApi(Long.class, BASE_URL + REGISTER, HttpStatus.CREATED, input);

        List<GroupsEntity> all = groupsRepository.findAll();
        assertEquals(all.size(), 1);

        GroupsEntity groupsEntity = all.get(0);
        var groupId = groupsEntity.getGroupId();

        BarrierEntity barrierEntity = barrierRepository.findByGroup_GroupId(groupId);
        assertEquals(barrierEntity.getDeviceId(), 2L);

        RfidDeviceEntity rfidEntity = rfidDevicesRepository.findByGroup_GroupId(groupId);
        assertEquals(rfidEntity.getDeviceId(), 1L);

        assertEquals(groupIdFromBody, groupsEntity.getGroupId());
    }

    @Test
    @DisplayName("написать тест, на регистрацию нового устройства, которого ранее не было в группе, сначала регаются 3, потом через время приходят 4, одно новое")
    public void retryRegistrationTest() {
        var input = buildRegisterDtoTwoDevices();

        var groupIdFromBody = testUtils.invokePostApi(Long.class, BASE_URL + REGISTER, HttpStatus.CREATED, input);

        List<GroupsEntity> all = groupsRepository.findAll();
        assertEquals(all.size(), 1);

        GroupsEntity groupsEntity = all.get(0);
        var groupId = groupsEntity.getGroupId();

        BarrierEntity barrierEntity = barrierRepository.findByGroup_GroupId(groupId);
        assertEquals(barrierEntity.getDeviceId(), 2L);

        RfidDeviceEntity rfidEntity = rfidDevicesRepository.findByGroup_GroupId(groupId);
        assertEquals(rfidEntity.getDeviceId(), 1L);

        assertEquals(groupIdFromBody, groupsEntity.getGroupId());

        //пушим с новым устройством
        var inputThreeDevice = buildRegisterDtoThreeDevices().setGroup(groupIdFromBody);

        var groupIdFromRetry = testUtils.invokePostApi(Long.class, BASE_URL + REGISTER, HttpStatus.CREATED, inputThreeDevice);

        List<GroupsEntity> afterRetry = groupsRepository.findAll();
        assertEquals(afterRetry.size(), 1);

        var listBarriers = barrierRepository.findAllByGroup_GroupId(groupIdFromRetry);
        var first = listBarriers.stream().filter(barrierEntity1 -> barrierEntity1.getDeviceId() == 3L).findFirst()
                .map(BarrierEntity::getDeviceId).get();
        assertEquals(first, 3L);

        assertEquals(listBarriers.size(), 2);
    }

    @Test
    @DisplayName("тест на регистрацию группы, при условии, что такая группа уже зарегистрирована")
    public void registrationWithExistedGroupAfterCoreCrashTest() {
        var input = buildRegisterDtoTwoDevices();

        var groupIdFromBody = testUtils.invokePostApi(Long.class, BASE_URL + REGISTER, HttpStatus.CREATED, input);

        List<GroupsEntity> all = groupsRepository.findAll();
        assertEquals(all.size(), 1);

        GroupsEntity groupsEntity = all.get(0);
        var groupId = groupsEntity.getGroupId();

        BarrierEntity barrierEntity = barrierRepository.findByGroup_GroupId(groupId);
        assertEquals(barrierEntity.getDeviceId(), 2L);

        RfidDeviceEntity rfidEntity = rfidDevicesRepository.findByGroup_GroupId(groupId);
        assertEquals(rfidEntity.getDeviceId(), 1L);

        assertEquals(groupIdFromBody, groupsEntity.getGroupId());

        //пушим с новым устройством
        var inputThreeDevice = buildRegisterDtoThreeDevices();

        var groupIdFromRetry = testUtils.invokePostApi(Long.class, BASE_URL + REGISTER, HttpStatus.CREATED, inputThreeDevice);

        List<GroupsEntity> afterRetry = groupsRepository.findAll();
        assertEquals(afterRetry.size(), 1);

        var listBarriers = barrierRepository.findAllByGroup_GroupId(groupIdFromRetry);
        var first = listBarriers.stream().filter(barrierEntity1 -> barrierEntity1.getDeviceId() == 3L).findFirst()
                .map(BarrierEntity::getDeviceId).get();

        var listRfids = rfidDevicesRepository.findAllByGroup_GroupId(groupIdFromRetry);
        assertEquals(first, 3L);
        assertEquals(groupIdFromRetry, groupIdFromBody);
        assertEquals(listBarriers.size(), 2);
        assertEquals(listRfids.size(), 1);
    }

    @Test
    public void registerTwoIdentityDevices(){
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            barrierRepository.save(new BarrierEntity().setDeviceId(123L).setType(DeviceType.ENTRY));
            barrierRepository.save(new BarrierEntity().setDeviceId(123L).setType(DeviceType.ENTRY));
        });

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            rfidDevicesRepository.save(new RfidDeviceEntity().setDeviceId(123L).setType(DeviceType.RFID));
            rfidDevicesRepository.save(new RfidDeviceEntity().setDeviceId(123L).setType(DeviceType.RFID));
        });

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            qrRepository.save(new QrEntity().setDeviceId(123L).setType(DeviceType.QR));
            qrRepository.save(new QrEntity().setDeviceId(123L).setType(DeviceType.QR));
        });

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            cardRepository.save(new CardEntity().setDeviceId(123L).setType(DeviceType.CARD));
            cardRepository.save(new CardEntity().setDeviceId(123L).setType(DeviceType.CARD));
        });
    }

    private RegistrationDto buildRegisterDtoTwoDevices() {
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

    private RegistrationDto buildRegisterDtoThreeDevices() {
        return new RegistrationDto()
                .setAddress("127.0.0.1")
                .setGroup(null)
                .setGroups(List.of(new DeviceDto()
                                .setId(1L)
                                .setType("RFID"),
                        new DeviceDto()
                                .setId(2L)
                                .setType("ENTRY"),
                        new DeviceDto()
                                .setId(3L)
                                .setType("ENTRY")));
    }
}