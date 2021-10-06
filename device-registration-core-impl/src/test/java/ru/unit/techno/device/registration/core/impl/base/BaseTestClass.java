
package ru.unit.techno.device.registration.core.impl.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.unit.techno.device.registration.core.impl.repository.*;

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
    protected QrRepository qrRepository;

    @Autowired
    protected CardRepository cardRepository;

    @Autowired
    protected RfidDevicesRepository rfidDevicesRepository;

    private static final String DB_NAME = "unit_techno";

    private static final PostgreSQLContainer postgresDB = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName(DB_NAME)
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5432)
            .withClasspathResourceMapping("init.sql", "/docker-entrypoint-initdb.d/init.sql", BindMode.READ_ONLY);

    static {
        postgresDB.start();
    }

    @AfterEach
    public void destroy() {
        barrierRepository.deleteAll();
        rfidDevicesRepository.deleteAll();
        groupsRepository.deleteAll();
    }
}