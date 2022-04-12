
package ru.unit.techno.device.registration.core.impl.resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import ru.unit.techno.device.registration.api.dto.DeviceDto;
import ru.unit.techno.device.registration.api.dto.RegistrationDto;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit.techno.device.registration.api.enums.SubType;
import ru.unit.techno.device.registration.core.impl.base.BaseTestClass;
import ru.unit.techno.device.registration.core.impl.entity.*;
import ru.unit.techno.device.registration.core.impl.service.RegistrationService;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegistrationControllerTest extends BaseTestClass {

    public static final String BASE_URL = "/api";
    public static final String REGISTER = "/registration";

    @Autowired
    private RegistrationService registrationService;

    @Test
    @DisplayName("когда устройства зарегались успешно")
    public void successRegistrationTest() {
        var input = buildRegisterDtoTwoDevices();

        testUtils.invokePostApi(Long.class, BASE_URL + REGISTER, HttpStatus.CREATED, input);

        List<GroupsEntity> all = groupsRepository.findAll();
        assertEquals(all.size(), 1);

        GroupsEntity groupsEntity = all.get(0);
        var groupId = groupsEntity.getGroupId();

        BarrierEntity barrierEntity = barrierRepository.findByGroup_GroupId(groupId);
        assertEquals(barrierEntity.getDeviceId(), 2L);

        RfidDeviceEntity rfidEntity = rfidDevicesRepository.findByGroup_GroupId(groupId);
        assertEquals(rfidEntity.getDeviceId(), 1L);

        assertEquals(input.getGroup(), groupsEntity.getGroupId());
    }

    @Test
    @DisplayName("написать тест, на регистрацию нового устройства, которого ранее не было в группе, сначала регаются 3, потом через время приходят 4, одно новое")
    public void retryRegistrationTest() {
        var input = buildRegisterDtoTwoDevices();

        var groupIdFromBody = "228L";
        testUtils.invokePostApi(Void.class, BASE_URL + REGISTER, HttpStatus.CREATED, input);

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

        var groupIdFromRetry = "228L";
        testUtils.invokePostApi(Void.class, BASE_URL + REGISTER, HttpStatus.CREATED, inputThreeDevice);

        List<GroupsEntity> afterRetry = groupsRepository.findAll();
        assertEquals(afterRetry.size(), 1);

        var listBarriers = barrierRepository.findAllByGroup_GroupId(groupIdFromRetry);
        var first = listBarriers.stream().filter(barrierEntity1 -> barrierEntity1.getDeviceId() == 2L).findFirst()
                .map(BarrierEntity::getDeviceId).get();
        assertEquals(first, 2L);

        assertEquals(listBarriers.size(), 1);
    }

    @Test
    @DisplayName("тест на регистрацию группы, при условии, что такая группа уже зарегистрирована")
    public void registrationWithExistedGroupAfterCoreCrashTest() {
        var input = buildRegisterDtoTwoDevices();

        var groupIdFromBody = "228L";
        testUtils.invokePostApi(Long.class, BASE_URL + REGISTER, HttpStatus.CREATED, input);

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

        var groupIdFromRetry = "228L";
        testUtils.invokePostApi(Long.class, BASE_URL + REGISTER, HttpStatus.CREATED, inputThreeDevice);

        QrEntity qrEntity = qrRepository.findByGroup_GroupId(groupId);
        assertEquals(qrEntity.getDeviceId(), 3L);

        List<GroupsEntity> afterRetry = groupsRepository.findAll();
        assertEquals(afterRetry.size(), 1);

        var listBarriers = barrierRepository.findAllByGroup_GroupId(groupIdFromRetry);
        var first = listBarriers.stream().filter(barrierEntity1 -> barrierEntity1.getDeviceId() == 2L).findFirst()
                .map(BarrierEntity::getDeviceId).get();

        var listRfids = rfidDevicesRepository.findAllByGroup_GroupId(groupIdFromRetry);
        var listQrs = qrRepository.findAllByGroup_GroupId(groupIdFromRetry);
        assertEquals(first, 2L);
        assertEquals(groupIdFromRetry, groupIdFromBody);
        assertEquals(listBarriers.size(), 1);
        assertEquals(listRfids.size(), 1);
        assertEquals(listQrs.size(), 1);
    }

    @Test
    public void registerTwoIdentityDevices() {
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

    @Test
    @DisplayName("Тест на ошибку при регистрации двух устройств одного типа в одну группу")
    public void registerTwoDevicesInOneGroup() {
        GroupsEntity gr1 = groupsRepository.save(new GroupsEntity()
                .setGroupId("1")
                .setAddress("Zalupa"));

        BarrierEntity bar1 =  new BarrierEntity().setDeviceId(789L).setGroup(gr1).setType(DeviceType.ENTRY);
        BarrierEntity bar2 =  new BarrierEntity().setDeviceId(222L).setGroup(gr1).setType(DeviceType.ENTRY);

        QrEntity qr1 = new QrEntity().setDeviceId(789L).setGroup(gr1).setType(DeviceType.QR);
        QrEntity qr2 = new QrEntity().setDeviceId(222L).setGroup(gr1).setType(DeviceType.QR);

        CardEntity card1 = new CardEntity().setDeviceId(789L).setGroup(gr1).setType(DeviceType.CARD);
        CardEntity card2 = new CardEntity().setDeviceId(222L).setGroup(gr1).setType(DeviceType.CARD);

        RfidDeviceEntity rfidOne = new RfidDeviceEntity().setDeviceId(789L).setGroup(gr1).setType(DeviceType.RFID);
        RfidDeviceEntity rfidTwo = new RfidDeviceEntity().setDeviceId(222L).setGroup(gr1).setType(DeviceType.RFID);

        rfidDevicesRepository.save(rfidOne);
        barrierRepository.save(bar1);
        qrRepository.save(qr1);
        cardRepository.save(card1);

        assertThrows(Exception.class,() -> barrierRepository.save(bar2));
        assertThrows(Exception.class,() -> cardRepository.save(card2));
        assertThrows(Exception.class,() -> qrRepository.save(qr2));
        assertThrows(Exception.class,() -> rfidDevicesRepository.save(rfidTwo));

        assertEquals(groupsRepository.findAll().size(), 1);
        assertEquals(barrierRepository.findAll().size(), 1);
        assertEquals(cardRepository.findAll().size(), 1);
        assertEquals(qrRepository.findAll().size(), 1);
        assertEquals(rfidDevicesRepository.findAll().size(), 1);
    }

    private RegistrationDto buildRegisterDtoTwoDevices() {
        return new RegistrationDto()
                .setAddress("127.0.0.1")
                .setGroup("228L")
                .setGroups(List.of(new DeviceDto()
                                .setId(1L)
                                .setType(DeviceType.RFID)
                                .setSubType(SubType.UNKNOWN),
                        new DeviceDto()
                                .setId(2L)
                                .setType(DeviceType.ENTRY)
                                .setSubType(SubType.UNKNOWN)));
    }

    private RegistrationDto buildRegisterDtoThreeDevices() {
        return new RegistrationDto()
                .setAddress("127.0.0.1")
                .setGroup("228L")
                .setGroups(List.of(new DeviceDto()
                                .setId(1L)
                                .setType(DeviceType.RFID)
                                .setSubType(SubType.UNKNOWN),
                        new DeviceDto()
                                .setId(2L)
                                .setType(DeviceType.ENTRY)
                                .setSubType(SubType.UNKNOWN),
                        new DeviceDto()
                                .setId(3L)
                                .setType(DeviceType.QR)
                                .setSubType(SubType.UNKNOWN)));
    }
}