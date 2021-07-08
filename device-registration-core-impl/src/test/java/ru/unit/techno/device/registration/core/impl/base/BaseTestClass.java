
package ru.unit.techno.device.registration.core.impl.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.unit.techno.device.registration.core.impl.repository.BarrierRepository;
import ru.unit.techno.device.registration.core.impl.repository.GroupsRepository;
import ru.unit.techno.device.registration.core.impl.repository.RfidDevicesRepository;

@Slf4j
@IntegrationTest
public class BaseTestClass {

    @Autowired
    protected TestUtils testUtils;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected BarrierRepository barrierRepository;

    @Autowired
    protected GroupsRepository groupsRepository;

    @Autowired
    protected RfidDevicesRepository rfidDevicesRepository;
}